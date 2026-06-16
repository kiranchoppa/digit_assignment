package digit.academy.tutorial.validator;

import digit.academy.tutorial.util.MdmsUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static digit.academy.tutorial.config.ServiceConstants.INVALID_ADVOCATE_TYPE;

@Component
public class AdvocateValidator {

    private final MdmsUtil mdmsUtil;

    public AdvocateValidator(MdmsUtil mdmsUtil) {
        this.mdmsUtil = mdmsUtil;
    }

    public void validateCreateRequest(AdvocateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("AdvocateRequest cannot be null");
        }

        if (request.getRequestInfo() == null) {
            throw new IllegalArgumentException("RequestInfo is required");
        }

        if (request.getRequestInfo().getUserInfo() == null) {
            throw new IllegalArgumentException("RequestInfo.userInfo is required");
        }

        if (!StringUtils.hasText(request.getRequestInfo().getUserInfo().getUuid())) {
            throw new IllegalArgumentException("RequestInfo.userInfo.uuid is required");
        }

        Advocate advocate = request.getAdvocate();
        if (advocate == null) {
            throw new IllegalArgumentException("Advocate is required");
        }

        if (!StringUtils.hasText(advocate.getTenantId())) {
            throw new IllegalArgumentException("Advocate tenantId is required");
        }

        if (!StringUtils.hasText(advocate.getIndividualId())) {
            throw new IllegalArgumentException("Advocate individualId is required");
        }

        if (!StringUtils.hasText(advocate.getAdvocateType())) {
            throw new IllegalArgumentException("Advocate advocateType is required");
        }

        if (!StringUtils.hasText(advocate.getBarRegistrationNumber())) {
            throw new IllegalArgumentException("Advocate barRegistrationNumber is required");
        }

        validateAdvocateType(request, advocate);
    }

    private void validateAdvocateType(AdvocateRequest request, Advocate advocate) {
        boolean isValidAdvocateType = mdmsUtil.isValidAdvocateType(
                request.getRequestInfo(),
                advocate.getTenantId(),
                advocate.getAdvocateType()
        );

        if (!isValidAdvocateType) {
            throw new CustomException(
                    INVALID_ADVOCATE_TYPE,
                    "Invalid advocateType: " + advocate.getAdvocateType()
            );
        }
    }
}
