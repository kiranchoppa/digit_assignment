package digit.academy.tutorial.web.controllers;

import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateListResponse;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import digit.academy.tutorial.web.models.AdvocateSearchRequest;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class AdvocateApiController {

    private final AdvocateService advocateService;

    @Autowired
    public AdvocateApiController(HttpServletRequest request, AdvocateService advocateService) {
        this.advocateService = advocateService;
    }

    @RequestMapping(value = "/advocate/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1CreatePost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Details for the advocate registration + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateRequest body) {

        Advocate advocate = advocateService.createAdvocate(body);
        return new ResponseEntity<>(advocateResponse(body.getRequestInfo(), Collections.singletonList(advocate)), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/advocate/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateRequest body) {

        Advocate advocate = advocateService.updateAdvocate(body);
        return ResponseEntity.ok(advocateResponse(body.getRequestInfo(), Collections.singletonList(advocate)));
    }

    @RequestMapping(value = "/advocate/v1/applicationnumber/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1ApplicationnumberSearchPost(
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "applicationNumber of advocate registration being searched", required = true, schema = @Schema())
        @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId", required = true, schema = @Schema())
        @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {

        List<Advocate> advocates = advocateService.searchAdvocates(tenantId, applicationNumber, null);
        return ResponseEntity.ok(advocateResponse(null, advocates));
    }

    @RequestMapping(value = "/advocate/v1/status/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1StatusSearchPost(
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "status of advocate registration being searched", required = true, schema = @Schema())
        @Valid @RequestParam(value = "status", required = true) String status,
        @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId", required = true, schema = @Schema())
        @Valid @RequestParam(value = "tenantId", required = true) String tenantId) {

        List<Advocate> advocates = advocateService.searchAdvocates(tenantId, null, status);
        return ResponseEntity.ok(advocateResponse(null, advocates));
    }

    @RequestMapping(value = "/advocate/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateListResponse> advocateV1SearchPost(
        @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema())
        @Valid @RequestBody AdvocateSearchRequest body) {

        List<AdvocateSearchCriteria> results = advocateService.searchAdvocates(body.getTenantId(), body.getCriteria());
        Pagination pagination = Pagination.builder()
            .offSet(0d)
            .limit(100d)
            .totalCount((double) results.stream().mapToInt(item -> item.getResponseList() == null ? 0 : item.getResponseList().size()).sum())
            .build();

        AdvocateListResponse response = AdvocateListResponse.builder()
            .responseInfo(responseInfo(body.getRequestInfo()))
            .advocates(results)
            .pagination(pagination)
            .build();
        return ResponseEntity.ok(response);
    }

    private AdvocateResponse advocateResponse(RequestInfo requestInfo, List<Advocate> advocates) {
        return AdvocateResponse.builder()
            .responseInfo(responseInfo(requestInfo))
            .advocates(advocates)
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
