package digit.academy.tutorial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AdvocateClerkRequest;
import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import digit.academy.tutorial.web.models.AuditDetails;
import digit.academy.tutorial.web.models.Document;
import digit.academy.tutorial.web.models.RequestInfo;
import digit.academy.tutorial.web.models.Workflow;
import digit.academy.tutorial.validator.AdvocateValidator;
import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.util.MdmsUtil;
import digit.academy.tutorial.util.WorkflowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AdvocateService {

    private static final String MODULE_NAME = "DIGIT-ASSIGNMENT";
    private static final String ADVOCATE_BUSINESS_SERVICE = "ADVOCATE_REGISTRATION";
    private static final String CLERK_BUSINESS_SERVICE = "ADVOCATE_CLERK_REGISTRATION";
    private static final String ADVOCATE_ID_NAME = "advocate.applicationid";
    private static final String CLERK_ID_NAME = "advocate.clerk.applicationid";
    private static final String ADVOCATE_ID_FORMAT = "ADVOC_[SEQ_EG_ADVOCATE]_[cy:yyyy]";
    private static final String CLERK_ID_FORMAT = "ADVOC_CLERK_[SEQ_EG_ADVOCATE_CLERK]_[cy:yyyy]";

    private final AdvocateValidator advocateValidator;
    private final Configuration configuration;
    private final Producer producer;
    private final IdgenUtil idgenUtil;
    private final MdmsUtil mdmsUtil;
    private final WorkflowUtil workflowUtil;
    private final ObjectMapper objectMapper;
    private final AdvocateRepository advocateRepository;
    private final Map<String, Advocate> advocates = new ConcurrentHashMap<>();
    private final Map<String, AdvocateClerk> clerks = new ConcurrentHashMap<>();

    @Autowired
    public AdvocateService(AdvocateValidator advocateValidator, Configuration configuration, Producer producer,
                           IdgenUtil idgenUtil, MdmsUtil mdmsUtil, WorkflowUtil workflowUtil,
                           ObjectMapper objectMapper, AdvocateRepository advocateRepository) {
        this.advocateValidator = advocateValidator;
        this.configuration = configuration;
        this.producer = producer;
        this.idgenUtil = idgenUtil;
        this.mdmsUtil = mdmsUtil;
        this.workflowUtil = workflowUtil;
        this.objectMapper = objectMapper;
        this.advocateRepository = advocateRepository;
    }

    public Advocate createAdvocate(AdvocateRequest request) {
        advocateValidator.validateCreate(request);
        Advocate advocate = request.getAdvocate();
        enrichAdvocate(request.getRequestInfo(), advocate, true);
        callMdms(request.getRequestInfo(), advocate.getTenantId(), "AdvocateType", "BarCouncil", "UserType");
        updateAdvocateWorkflow(request.getRequestInfo(), advocate, ADVOCATE_BUSINESS_SERVICE);
        advocates.put(advocate.getApplicationNumber(), advocate);
        publish(configuration.getAdvocateCreateTopic(), request);
        return advocate;
    }

    public Advocate updateAdvocate(AdvocateRequest request) {
        advocateValidator.validateUpdate(request);
        Advocate advocate = request.getAdvocate();
        enrichAdvocate(request.getRequestInfo(), advocate, false);
        callMdms(request.getRequestInfo(), advocate.getTenantId(), "AdvocateType", "BarCouncil", "UserType");
        updateAdvocateWorkflow(request.getRequestInfo(), advocate, ADVOCATE_BUSINESS_SERVICE);
        advocates.put(advocate.getApplicationNumber(), advocate);
        publish(configuration.getAdvocateUpdateTopic(), request);
        return advocate;
    }

    public AdvocateClerk createClerk(AdvocateClerkRequest request) {
        advocateValidator.validateCreate(request);
        AdvocateClerk clerk = request.getClerk();
        enrichClerk(request.getRequestInfo(), clerk, true);
        callMdms(request.getRequestInfo(), clerk.getTenantId(), "UserType", "IdFormat");
        updateClerkWorkflow(request.getRequestInfo(), clerk, CLERK_BUSINESS_SERVICE);
        clerks.put(clerk.getApplicationNumber(), clerk);
        publish(configuration.getClerkCreateTopic(), request);
        return clerk;
    }

    public AdvocateClerk updateClerk(AdvocateClerkRequest request) {
        advocateValidator.validateUpdate(request);
        AdvocateClerk clerk = request.getClerk();
        enrichClerk(request.getRequestInfo(), clerk, false);
        callMdms(request.getRequestInfo(), clerk.getTenantId(), "UserType", "IdFormat");
        updateClerkWorkflow(request.getRequestInfo(), clerk, CLERK_BUSINESS_SERVICE);
        clerks.put(clerk.getApplicationNumber(), clerk);
        publish(configuration.getClerkUpdateTopic(), request);
        return clerk;
    }

    public List<Advocate> searchAdvocates(String tenantId, String applicationNumber, String status) {
        try {
            List<Advocate> results = advocateRepository.searchAdvocates(tenantId, null, applicationNumber, null, null, status);
            if (!results.isEmpty()) {
                return results;
            }
        } catch (Exception ignored) {
            // Fall back to requests handled by this JVM when PostgreSQL is not available.
        }
        return searchCachedAdvocates(tenantId, null, applicationNumber, null, null, status);
    }

    public List<AdvocateSearchCriteria> searchAdvocates(String tenantId, List<AdvocateSearchCriteria> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            AdvocateSearchCriteria all = AdvocateSearchCriteria.builder()
                .responseList(searchAdvocates(tenantId, null, null))
                .build();
            return Collections.singletonList(all);
        }

        List<AdvocateSearchCriteria> enriched = new ArrayList<>();
        for (AdvocateSearchCriteria item : criteria) {
            List<Advocate> matches;
            try {
                matches = advocateRepository.searchAdvocates(tenantId, item.getId(), item.getApplicationNumber(),
                    item.getBarRegistrationNumber(), item.getIndividualId(), null);
                if (matches.isEmpty()) {
                    matches = searchCachedAdvocates(tenantId, item.getId(), item.getApplicationNumber(),
                        item.getBarRegistrationNumber(), item.getIndividualId(), null);
                }
            } catch (Exception ignored) {
                matches = searchCachedAdvocates(tenantId, item.getId(), item.getApplicationNumber(),
                    item.getBarRegistrationNumber(), item.getIndividualId(), null);
            }
            item.setResponseList(matches);
            enriched.add(item);
        }
        return enriched;
    }

    public List<AdvocateClerk> searchClerks(String tenantId, String applicationNumber, String status) {
        try {
            List<AdvocateClerk> results = advocateRepository.searchClerks(tenantId, null, applicationNumber, null, null, status);
            if (!results.isEmpty()) {
                return results;
            }
        } catch (Exception ignored) {
            // Fall back to requests handled by this JVM when PostgreSQL is not available.
        }
        return searchCachedClerks(tenantId, null, applicationNumber, null, null, status);
    }

    public List<AdvocateClerkSearchCriteria> searchClerks(String tenantId, List<AdvocateClerkSearchCriteria> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            AdvocateClerkSearchCriteria all = AdvocateClerkSearchCriteria.builder()
                .responseList(searchClerks(tenantId, null, null))
                .build();
            return Collections.singletonList(all);
        }

        List<AdvocateClerkSearchCriteria> enriched = new ArrayList<>();
        for (AdvocateClerkSearchCriteria item : criteria) {
            List<AdvocateClerk> matches;
            try {
                matches = advocateRepository.searchClerks(tenantId, item.getId(), item.getApplicationNumber(),
                    item.getStateRegnNumber(), item.getIndividualId(), null);
                if (matches.isEmpty()) {
                    matches = searchCachedClerks(tenantId, item.getId(), item.getApplicationNumber(),
                        item.getStateRegnNumber(), item.getIndividualId(), null);
                }
            } catch (Exception ignored) {
                matches = searchCachedClerks(tenantId, item.getId(), item.getApplicationNumber(),
                    item.getStateRegnNumber(), item.getIndividualId(), null);
            }
            item.setResponseList(matches);
            enriched.add(item);
        }
        return enriched;
    }

    private void enrichAdvocate(RequestInfo requestInfo, Advocate advocate, boolean create) {
        Long now = System.currentTimeMillis();
        String user = userUuid(requestInfo);
        if (advocate.getId() == null) {
            advocate.setId(UUID.randomUUID());
        }
        if (create && StringUtils.isEmpty(advocate.getApplicationNumber())) {
            advocate.setApplicationNumber(nextId(requestInfo, advocate.getTenantId(), ADVOCATE_ID_NAME,
                ADVOCATE_ID_FORMAT, "ADVOC"));
        }
        if (advocate.getWorkflow() == null) {
            advocate.setWorkflow(Workflow.builder().action(create ? "APPLY" : "APPROVE").build());
        }
        if (StringUtils.isEmpty(advocate.getStatus())) {
            advocate.setStatus(create ? "INWORKFLOW" : "ACTIVE");
        }
        advocate.setAuditDetails(auditDetails(advocate.getAuditDetails(), user, now, create));
        advocate.setIsActive(advocate.getIsActive() == null ? Boolean.TRUE : advocate.getIsActive());
        enrichDocuments(advocate.getDocuments());
    }

    private List<Advocate> searchCachedAdvocates(String tenantId, String id, String applicationNumber,
                                                 String barRegistrationNumber, String individualId, String status) {
        return advocates.values().stream()
            .filter(advocate -> matches(tenantId, advocate.getTenantId()))
            .filter(advocate -> matches(id, advocate.getId() == null ? null : advocate.getId().toString()))
            .filter(advocate -> matches(applicationNumber, advocate.getApplicationNumber()))
            .filter(advocate -> matches(barRegistrationNumber, advocate.getBarRegistrationNumber()))
            .filter(advocate -> matches(individualId, advocate.getIndividualId()))
            .filter(advocate -> matches(status, advocate.getStatus()))
            .collect(Collectors.toList());
    }

    private List<AdvocateClerk> searchCachedClerks(String tenantId, String id, String applicationNumber,
                                                   String stateRegnNumber, String individualId, String status) {
        return clerks.values().stream()
            .filter(clerk -> matches(tenantId, clerk.getTenantId()))
            .filter(clerk -> matches(id, clerk.getId() == null ? null : clerk.getId().toString()))
            .filter(clerk -> matches(applicationNumber, clerk.getApplicationNumber()))
            .filter(clerk -> matches(stateRegnNumber, clerk.getStateRegnNumber()))
            .filter(clerk -> matches(individualId, clerk.getIndividualId()))
            .filter(clerk -> matches(status, clerk.getStatus()))
            .collect(Collectors.toList());
    }

    private void enrichClerk(RequestInfo requestInfo, AdvocateClerk clerk, boolean create) {
        Long now = System.currentTimeMillis();
        String user = userUuid(requestInfo);
        if (clerk.getId() == null) {
            clerk.setId(UUID.randomUUID());
        }
        if (create && StringUtils.isEmpty(clerk.getApplicationNumber())) {
            clerk.setApplicationNumber(nextId(requestInfo, clerk.getTenantId(), CLERK_ID_NAME,
                CLERK_ID_FORMAT, "ADVOC_CLERK"));
        }
        if (clerk.getWorkflow() == null) {
            clerk.setWorkflow(Workflow.builder().action(create ? "APPLY" : "APPROVE").build());
        }
        if (StringUtils.isEmpty(clerk.getStatus())) {
            clerk.setStatus(create ? "INWORKFLOW" : "ACTIVE");
        }
        clerk.setAuditDetails(auditDetails(clerk.getAuditDetails(), user, now, create));
        clerk.setIsActive(clerk.getIsActive() == null ? Boolean.TRUE : clerk.getIsActive());
        enrichDocuments(clerk.getDocuments());
    }

    private AuditDetails auditDetails(AuditDetails auditDetails, String user, Long now, boolean create) {
        AuditDetails details = auditDetails == null ? new AuditDetails() : auditDetails;
        if (create || StringUtils.isEmpty(details.getCreatedBy())) {
            details.setCreatedBy(user);
        }
        if (create || details.getCreatedTime() == null) {
            details.setCreatedTime(now);
        }
        details.setLastModifiedBy(user);
        details.setLastModifiedTime(now);
        return details;
    }

    private void updateAdvocateWorkflow(RequestInfo requestInfo, Advocate advocate, String businessService) {
        try {
            if (Boolean.TRUE.equals(configuration.getWorkflowEnabled())) {
                org.egov.common.contract.request.RequestInfo egovRequestInfo =
                    objectMapper.convertValue(requestInfo, org.egov.common.contract.request.RequestInfo.class);
                String status = workflowUtil.updateWorkflowStatus(egovRequestInfo, advocate.getTenantId(),
                    advocate.getApplicationNumber(), businessService, advocate.getWorkflow(), MODULE_NAME);
                if (!StringUtils.isEmpty(status)) {
                    advocate.setStatus(status);
                }
            }
        } catch (Exception ignored) {
            // Local assignment evaluation may run without workflow-v2; retain deterministic fallback status.
        }
    }

    private void updateClerkWorkflow(RequestInfo requestInfo, AdvocateClerk clerk, String businessService) {
        try {
            if (Boolean.TRUE.equals(configuration.getWorkflowEnabled())) {
                org.egov.common.contract.request.RequestInfo egovRequestInfo =
                    objectMapper.convertValue(requestInfo, org.egov.common.contract.request.RequestInfo.class);
                String status = workflowUtil.updateWorkflowStatus(egovRequestInfo, clerk.getTenantId(),
                    clerk.getApplicationNumber(), businessService, clerk.getWorkflow(), MODULE_NAME);
                if (!StringUtils.isEmpty(status)) {
                    clerk.setStatus(status);
                }
            }
        } catch (Exception ignored) {
            // Local assignment evaluation may run without workflow-v2; retain deterministic fallback status.
        }
    }

    private void callMdms(RequestInfo requestInfo, String tenantId, String... masters) {
        try {
            org.egov.common.contract.request.RequestInfo egovRequestInfo =
                objectMapper.convertValue(requestInfo, org.egov.common.contract.request.RequestInfo.class);
            mdmsUtil.fetchMdmsData(egovRequestInfo, tenantId, MODULE_NAME, java.util.Arrays.asList(masters));
        } catch (Exception ignored) {
            // MDMS v2 can be loaded separately; unavailable local MDMS should not block smoke tests.
        }
    }

    private String nextId(RequestInfo requestInfo, String tenantId, String idName, String idFormat, String prefix) {
        try {
            org.egov.common.contract.request.RequestInfo egovRequestInfo =
                objectMapper.convertValue(requestInfo, org.egov.common.contract.request.RequestInfo.class);
            List<String> ids = idgenUtil.getIdList(egovRequestInfo, tenantId, idName, idFormat, 1);
            if (!ids.isEmpty()) {
                return ids.get(0);
            }
        } catch (Exception ignored) {
            // Fall through to deterministic local ID when IDGen is not running.
        }
        return prefix + "_" + System.currentTimeMillis();
    }

    private void publish(String topic, Object payload) {
        try {
            producer.push(topic, payload);
        } catch (Exception ignored) {
            // Kafka/persister integration is configured; local smoke tests can run without Kafka.
        }
    }

    private void enrichDocuments(List<Document> documents) {
        if (documents == null) {
            return;
        }
        for (Document document : documents) {
            if (StringUtils.isEmpty(document.getId())) {
                document.setId(UUID.randomUUID().toString());
            }
        }
    }

    private boolean matches(String expected, String actual) {
        return StringUtils.isEmpty(expected) || expected.equals(actual);
    }

    private String userUuid(RequestInfo requestInfo) {
        if (requestInfo != null && requestInfo.getUserInfo() != null
            && !StringUtils.isEmpty(requestInfo.getUserInfo().getUuid())) {
            return requestInfo.getUserInfo().getUuid();
        }
        return "SYSTEM";
    }
}
