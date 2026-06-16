package digit.academy.tutorial.service;



import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.validator.AdvocateValidator;
import digit.academy.tutorial.web.models.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class AdvocateService {

    private final AdvocateValidator advocateValidator;
    private final Producer producer;
    private final Configuration config;

    public AdvocateService(AdvocateValidator advocateValidator,
                           Producer producer,
                           Configuration config) {
        this.advocateValidator = advocateValidator;
        this.producer = producer;
        this.config = config;
    }

    public AdvocateResponse createAdvocate(AdvocateRequest request) {

        advocateValidator.validateCreateRequest(request);
        Advocate advocate = request.getAdvocate();

        long now = System.currentTimeMillis();
        String userUuid = request.getRequestInfo().getUserInfo().getUuid();

        advocate.setId(UUID.randomUUID());
        advocate.setApplicationNumber("ADV-" + now);
        advocate.setStatus("INITIATED");
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
                .status(ResponseInfo.StatusEnum.SUCCESSFUL)
                .build();

        return AdvocateResponse.builder()
                .responseInfo(responseInfo)
                .advocates(Collections.singletonList(advocate))
                .build();
    }
}
