package digit.academy.tutorial.util;

import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.web.models.RequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static digit.academy.tutorial.config.ServiceConstants.*;

@Slf4j
@Component
public class MdmsUtil {

    private static final String REQUEST_INFO = "RequestInfo";
    private static final String MDMS_CRITERIA = "MdmsCriteria";
    private static final String TENANT_ID = "tenantId";
    private static final String SCHEMA_CODE = "schemaCode";
    private static final String FILTERS = "filters";
    private static final String CODE = "code";
    private static final String IS_ACTIVE = "isActive";
    private static final String MDMS_RESPONSE = "mdms";

    private final RestTemplate restTemplate;
    private final Configuration configs;

    public MdmsUtil(RestTemplate restTemplate, Configuration configs) {
        this.restTemplate = restTemplate;
        this.configs = configs;
    }

    public boolean isValidAdvocateType(RequestInfo requestInfo, String tenantId, String advocateType) {
        Map<String, Object> request = buildMdmsSearchRequest(requestInfo, tenantId, advocateType);
        String uri = configs.getMdmsHost() + configs.getMdmsEndPoint();

        try {
            Map<String, Object> response = restTemplate.postForObject(uri, request, Map.class);
            if (response == null || !(response.get(MDMS_RESPONSE) instanceof List)) {
                throw new CustomException(MDMS_SERVICE_ERROR, "Invalid response received from MDMS");
            }

            List<?> mdmsData = (List<?>) response.get(MDMS_RESPONSE);
            return !mdmsData.isEmpty();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
            throw new CustomException(MDMS_SERVICE_ERROR, "Unable to validate advocateType from MDMS");
        }
    }

    private Map<String, Object> buildMdmsSearchRequest(RequestInfo requestInfo, String tenantId, String advocateType) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(CODE, advocateType);

        Map<String, Object> mdmsCriteria = new HashMap<>();
        mdmsCriteria.put(TENANT_ID, tenantId);
        mdmsCriteria.put(SCHEMA_CODE, ADVOCATE_TYPE_SCHEMA_CODE);
        mdmsCriteria.put(FILTERS, filters);
        mdmsCriteria.put(IS_ACTIVE, true);

        Map<String, Object> request = new HashMap<>();
        request.put(REQUEST_INFO, requestInfo);
        request.put(MDMS_CRITERIA, mdmsCriteria);

        return request;
    }
}
