package digit.academy.tutorial.web.controllers;

import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AdvocateClerkListResponse;
import digit.academy.tutorial.web.models.AdvocateClerkRequest;
import digit.academy.tutorial.web.models.AdvocateClerkResponse;
import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import digit.academy.tutorial.web.models.AdvocateClerkSearchRequest;
import digit.academy.tutorial.web.models.Pagination;
import digit.academy.tutorial.web.models.RequestInfo;
import digit.academy.tutorial.web.models.ResponseInfo;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ClerkApiController {

    private final AdvocateService advocateService;

    @Autowired
    public ClerkApiController(HttpServletRequest request, AdvocateService advocateService) {
        this.advocateService = advocateService;
    }

    @RequestMapping(value = "/clerk/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1CreatePost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Details for the user registration + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateClerkRequest body) {

        AdvocateClerk clerk = advocateService.createClerk(body);
        return new ResponseEntity<>(clerkResponse(body.getRequestInfo(), Collections.singletonList(clerk)), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/clerk/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1UpdatePost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateClerkRequest body) {

        AdvocateClerk clerk = advocateService.updateClerk(body);
        return ResponseEntity.ok(clerkResponse(body.getRequestInfo(), Collections.singletonList(clerk)));
    }

    @RequestMapping(value = "/clerk/v1/applicationnumber/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1ApplicationnumberSearchPost(
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "applicationNumber of clerks registration being searched", required = true, schema = @Schema())
        @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId", required = true, schema = @Schema())
        @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {

        List<AdvocateClerk> clerks = advocateService.searchClerks(tenantId, applicationNumber, null);
        return ResponseEntity.ok(clerkResponse(null, clerks));
    }

    @RequestMapping(value = "/clerk/v1/status/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1StatusSearchPost(
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "status of clerks registration being searched", required = true, schema = @Schema())
        @Valid @RequestParam(value = "status", required = true) String status,
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId", required = true, schema = @Schema())
        @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {

        List<AdvocateClerk> clerks = advocateService.searchClerks(tenantId, null, status);
        return ResponseEntity.ok(clerkResponse(null, clerks));
    }

    @RequestMapping(value = "/clerk/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkListResponse> clerkV1SearchPost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateClerkSearchRequest body) {

        List<AdvocateClerkSearchCriteria> results = advocateService.searchClerks(body.getTenantId(), body.getCriteria());
        Pagination pagination = Pagination.builder()
            .offSet(0d)
            .limit(100d)
            .totalCount((double) results.stream().mapToInt(item -> item.getResponseList() == null ? 0 : item.getResponseList().size()).sum())
            .build();

        AdvocateClerkListResponse response = AdvocateClerkListResponse.builder()
            .responseInfo(responseInfo(body.getRequestInfo()))
            .clerks(results)
            .pagination(pagination)
            .build();
        return ResponseEntity.ok(response);
    }

    private AdvocateClerkResponse clerkResponse(RequestInfo requestInfo, List<AdvocateClerk> clerks) {
        return AdvocateClerkResponse.builder()
            .responseInfo(responseInfo(requestInfo))
            .clerks(clerks)
            .build();
    }

    private ResponseInfo responseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder()
            .apiId(requestInfo == null ? "Rainmaker" : requestInfo.getApiId())
            .ver(requestInfo == null ? "1.0" : requestInfo.getVer())
            .ts(requestInfo == null ? System.currentTimeMillis() : requestInfo.getTs())
            .msgId(requestInfo == null ? null : requestInfo.getMsgId())
            .resMsgId(UUID.randomUUID().toString())
            .status(ResponseInfo.StatusEnum.SUCCESSFUL)
            .build();
    }
}
