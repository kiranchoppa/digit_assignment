package digit.academy.tutorial.web.controllers;


import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.web.models.AdvocateListResponse;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import digit.academy.tutorial.web.models.AdvocateSearchRequest;
import digit.academy.tutorial.web.models.ErrorResponse;
    import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import javax.validation.constraints.*;
    import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;
        import java.util.Optional;
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Controller
    @RequestMapping("")
    public class AdvocateApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        private final AdvocateService advocateService;

        @Autowired
        public AdvocateApiController(ObjectMapper objectMapper,
                                     HttpServletRequest request,
                                     AdvocateService advocateService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.advocateService = advocateService;
        }

                @RequestMapping(value="/advocate/v1/applicationnumber/_search", method = RequestMethod.POST)
                public ResponseEntity<AdvocateResponse> advocateV1ApplicationnumberSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "applicationNumber of advocate registration being searched" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,@NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<AdvocateResponse>(objectMapper.readValue("{  \"advocates\" : [ {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  }, {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", AdvocateResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/advocate/v1/_create", method = RequestMethod.POST)
                public ResponseEntity<AdvocateResponse> advocateV1CreatePost(
                        @Parameter(in = ParameterIn.DEFAULT, description = "Details for the advocate registration + RequestInfo meta data.", required=true, schema=@Schema())
                        @Valid @RequestBody AdvocateRequest body) {

                    AdvocateResponse response = advocateService.createAdvocate(body);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                }

                @RequestMapping(value="/advocate/v1/_search", method = RequestMethod.POST)
                public ResponseEntity<AdvocateListResponse> advocateV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody AdvocateSearchRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<AdvocateListResponse>(objectMapper.readValue("{  \"pagination\" : {    \"offSet\" : 6.027456183070403,    \"limit\" : 8.008281904610115,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 1.4658129805029452,    \"order\" : \"\"  },  \"advocates\" : [ {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"responseList\" : [ {      \"barRegistrationNumber\" : \"barRegistrationNumber\",      \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",      \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"workflow\" : {        \"documents\" : [ {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        }, {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        } ],        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\",        \"status\" : \"status\"      },      \"applicationNumber\" : \"applicationNumber\",      \"documents\" : [ null, null ],      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : { },      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"status\" : \"status\"    }, {      \"barRegistrationNumber\" : \"barRegistrationNumber\",      \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",      \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"workflow\" : {        \"documents\" : [ {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        }, {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        } ],        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\",        \"status\" : \"status\"      },      \"applicationNumber\" : \"applicationNumber\",      \"documents\" : [ null, null ],      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : { },      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"status\" : \"status\"    } ],    \"id\" : \"id\",    \"individualId\" : \"individualId\"  }, {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"responseList\" : [ {      \"barRegistrationNumber\" : \"barRegistrationNumber\",      \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",      \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"workflow\" : {        \"documents\" : [ {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        }, {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        } ],        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\",        \"status\" : \"status\"      },      \"applicationNumber\" : \"applicationNumber\",      \"documents\" : [ null, null ],      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : { },      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"status\" : \"status\"    }, {      \"barRegistrationNumber\" : \"barRegistrationNumber\",      \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",      \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"workflow\" : {        \"documents\" : [ {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        }, {          \"documentType\" : \"documentType\",          \"documentUid\" : \"documentUid\",          \"fileStore\" : \"fileStore\",          \"id\" : \"id\",          \"additionalDetails\" : { }        } ],        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\",        \"status\" : \"status\"      },      \"applicationNumber\" : \"applicationNumber\",      \"documents\" : [ null, null ],      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : { },      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"status\" : \"status\"    } ],    \"id\" : \"id\",    \"individualId\" : \"individualId\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", AdvocateListResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<AdvocateListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<AdvocateListResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/advocate/v1/status/_search", method = RequestMethod.POST)
                public ResponseEntity<AdvocateResponse> advocateV1StatusSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "status of advocate registration being searched" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "status", required = true) String status,@NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<AdvocateResponse>(objectMapper.readValue("{  \"advocates\" : [ {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  }, {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", AdvocateResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/advocate/v1/_update", method = RequestMethod.POST)
                public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody AdvocateRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<AdvocateResponse>(objectMapper.readValue("{  \"advocates\" : [ {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  }, {    \"barRegistrationNumber\" : \"barRegistrationNumber\",    \"advocateType\" : \"PROSECUTOR, PUBLIC DEFENDER\",    \"organisationID\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"workflow\" : {      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\",      \"status\" : \"status\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ null, null ],    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", AdvocateResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
