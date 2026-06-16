package digit.academy.tutorial.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.validator.AdvocateValidator;
import digit.academy.tutorial.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class AdvocateService {

    private final AdvocateValidator advocateValidator;
    private final Producer producer;
    private final Configuration config;
    private final IdgenUtil idgenUtil;
    private final ObjectMapper objectMapper;

    public AdvocateService(AdvocateValidator advocateValidator,
                           Producer producer,
                           Configuration config, IdgenUtil idgenUtil, ObjectMapper objectMapper) {
        this.advocateValidator = advocateValidator;
        this.producer = producer;
        this.config = config;
        this.idgenUtil = idgenUtil;
        this.objectMapper = objectMapper;
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
        advocate.setStatus("REGISTRATION_REQUESTED");
        advocate.setIsActive(true);

        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy(userUuid)
                .createdTime(now)
                .lastModifiedBy(userUuid)
                .lastModifiedTime(now)
                .build();

        advocate.setAuditDetails(auditDetails);
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
}
