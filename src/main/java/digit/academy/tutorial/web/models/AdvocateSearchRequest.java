package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import digit.academy.tutorial.web.models.RequestInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * advocate search is based on an array of criteria object.
 */
@Schema(description = "advocate search is based on an array of criteria object.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateSearchRequest   {
        @JsonProperty("RequestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("tenantId")

                private String tenantId = null;

        @JsonProperty("criteria")
          @Valid
                private List<AdvocateSearchCriteria> criteria = null;


        public AdvocateSearchRequest addCriteriaItem(AdvocateSearchCriteria criteriaItem) {
            if (this.criteria == null) {
            this.criteria = new ArrayList<>();
            }
        this.criteria.add(criteriaItem);
        return this;
        }

}
