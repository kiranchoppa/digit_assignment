package digit.academy.tutorial.validator;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AdvocateValidator {

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
    }
}
