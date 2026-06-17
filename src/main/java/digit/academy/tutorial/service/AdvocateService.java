package digit.academy.tutorial.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.util.WorkflowUtil;
import digit.academy.tutorial.validator.AdvocateValidator;
import digit.academy.tutorial.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static digit.academy.tutorial.config.ServiceConstants.ADVOCATE_BUSINESS_SERVICE;
import static digit.academy.tutorial.config.ServiceConstants.ADVOCATE_REGISTER_ACTION;
import static digit.academy.tutorial.config.ServiceConstants.ADVOCATE_WORKFLOW_MODULE_NAME;

@Service
public class AdvocateService {

    private final AdvocateValidator advocateValidator;
    private final Producer producer;
    private final Configuration config;
    private final IdgenUtil idgenUtil;
    private final WorkflowUtil workflowUtil;
    private final ObjectMapper objectMapper;
    private final AdvocateRepository advocateRepository;

    public AdvocateService(AdvocateValidator advocateValidator,
                           Producer producer,
                           Configuration config,
                           IdgenUtil idgenUtil,
                           WorkflowUtil workflowUtil,
                           ObjectMapper objectMapper,
                           AdvocateRepository advocateRepository) {
        this.advocateValidator = advocateValidator;
        this.producer = producer;
        this.config = config;
        this.idgenUtil = idgenUtil;
        this.workflowUtil = workflowUtil;
        this.objectMapper = objectMapper;
        this.advocateRepository = advocateRepository;
    }

    public AdvocateResponse createAdvocate(AdvocateRequest request) {

        advocateValidator.validateCreateRequest(request);
        Advocate advocate = request.getAdvocate();

        long now = System.currentTimeMillis();
        String userUuid = request.getRequestInfo().getUserInfo().getUuid();

        RequestInfo commonRequestInfo = objectMapper.convertValue(
                request.getRequestInfo(),
                RequestInfo.class
        );

        String applicationNumber = idgenUtil.getIdList(
                commonRequestInfo,
                advocate.getTenantId(),
                "advocate.applicationnumber",
                null,
                1
        ).get(0);

        advocate.setId(UUID.randomUUID());
        advocate.setApplicationNumber(applicationNumber);
        advocate.setIsActive(true);

        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy(userUuid)
                .createdTime(now)
                .lastModifiedBy(userUuid)
                .lastModifiedTime(now)
                .build();

        advocate.setAuditDetails(auditDetails);

        Workflow registerWorkflow = Workflow.builder()
                .action(ADVOCATE_REGISTER_ACTION)
                .build();

        String workflowStatus = workflowUtil.updateWorkflowStatus(
                commonRequestInfo,
                advocate.getTenantId(),
                advocate.getApplicationNumber(),
                ADVOCATE_BUSINESS_SERVICE,
                registerWorkflow,
                ADVOCATE_WORKFLOW_MODULE_NAME
        );

        advocate.setWorkflow(registerWorkflow);
        advocate.setStatus(workflowStatus);
        producer.push(config.getAdvocateCreateTopic(), request);

        ResponseInfo responseInfo = ResponseInfo.builder()
                .apiId(request.getRequestInfo().getApiId())
                .ver(request.getRequestInfo().getVer())
                .ts(System.currentTimeMillis())
                .msgId(request.getRequestInfo().getMsgId())
                .status(ResponseInfo.StatusEnum.SUCCESSFUL)
                .build();

        return AdvocateResponse.builder()
                .responseInfo(responseInfo)
                .advocates(Collections.singletonList(advocate))
                .build();
    }

    public AdvocateListResponse searchAdvocates(AdvocateSearchRequest request) {
        advocateValidator.validateSearchRequest(request);

        List<AdvocateSearchCriteria> responseCriteria = new ArrayList<>();

        if (CollectionUtils.isEmpty(request.getCriteria())) {
            AdvocateSearchCriteria emptyCriteria = AdvocateSearchCriteria.builder().build();
            emptyCriteria.setResponseList(advocateRepository.search(request.getTenantId(), null));
            responseCriteria.add(emptyCriteria);
        } else {
            for (AdvocateSearchCriteria criteria : request.getCriteria()) {
                AdvocateSearchCriteria resultCriteria = AdvocateSearchCriteria.builder()
                        .id(criteria.getId())
                        .applicationNumber(criteria.getApplicationNumber())
                        .barRegistrationNumber(criteria.getBarRegistrationNumber())
                        .individualId(criteria.getIndividualId())
                        .responseList(advocateRepository.search(request.getTenantId(), criteria))
                        .build();
                responseCriteria.add(resultCriteria);
            }
        }

        ResponseInfo responseInfo = ResponseInfo.builder()
                .apiId(request.getRequestInfo().getApiId())
                .ver(request.getRequestInfo().getVer())
                .ts(System.currentTimeMillis())
                .msgId(request.getRequestInfo().getMsgId())
                .status(ResponseInfo.StatusEnum.SUCCESSFUL)
                .build();

        int totalCount = responseCriteria.stream()
                .map(AdvocateSearchCriteria::getResponseList)
                .filter(list -> list != null)
                .mapToInt(List::size)
                .sum();

        Pagination pagination = Pagination.builder()
                .offSet(0d)
                .limit((double) totalCount)
                .totalCount((double) totalCount)
                .sortBy("createdTime")
                .order(Order.DESC)
                .build();

        return AdvocateListResponse.builder()
                .responseInfo(responseInfo)
                .advocates(responseCriteria)
                .pagination(pagination)
                .build();
    }
}
