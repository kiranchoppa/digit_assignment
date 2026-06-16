package digit.academy.tutorial.validator;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AdvocateClerkRequest;
import digit.academy.tutorial.web.models.AdvocateRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AdvocateValidator {

    private static final String INVALID_ADVOCATE_CREATE_REQUEST = "INVALID_ADVOCATE_CREATE_REQUEST";

    public void validateCreate(AdvocateRequest request) {
        validateAdvocateRequest(request, true);
    }

    public void validateUpdate(AdvocateRequest request) {
        validateAdvocateRequest(request, false);
    }

    public void validateCreate(AdvocateClerkRequest request) {
        validateClerkRequest(request, true);
    }

    public void validateUpdate(AdvocateClerkRequest request) {
        validateClerkRequest(request, false);
    }

    private void validateAdvocateRequest(AdvocateRequest request, boolean create) {
        if (request == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "Request body is mandatory");
        }

        if (request.getRequestInfo() == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "RequestInfo is mandatory");
        }

        Advocate advocate = request.getAdvocate();
        if (advocate == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "Advocate details are mandatory");
        }

        if (StringUtils.isEmpty(advocate.getTenantId())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "tenantId is mandatory");
        }

        if (StringUtils.isEmpty(advocate.getIndividualId())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "individualId is mandatory");
        }

        if (StringUtils.isEmpty(advocate.getAdvocateType())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "advocateType is mandatory");
        }

        if (!create && advocate.getId() == null && StringUtils.isEmpty(advocate.getApplicationNumber())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "id or applicationNumber is mandatory for update");
        }
    }

    private void validateClerkRequest(AdvocateClerkRequest request, boolean create) {
        if (request == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "Request body is mandatory");
        }

        if (request.getRequestInfo() == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "RequestInfo is mandatory");
        }

        AdvocateClerk clerk = request.getClerk();
        if (clerk == null) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "Clerk details are mandatory");
        }

        if (StringUtils.isEmpty(clerk.getTenantId())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "tenantId is mandatory");
        }

        if (StringUtils.isEmpty(clerk.getIndividualId())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "individualId is mandatory");
        }

        if (!create && clerk.getId() == null && StringUtils.isEmpty(clerk.getApplicationNumber())) {
            throw new CustomException(INVALID_ADVOCATE_CREATE_REQUEST, "id or applicationNumber is mandatory for update");
        }
    }
}
