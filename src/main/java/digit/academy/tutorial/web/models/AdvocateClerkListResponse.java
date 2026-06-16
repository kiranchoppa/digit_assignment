package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import digit.academy.tutorial.web.models.Pagination;
import digit.academy.tutorial.web.models.ResponseInfo;
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
 * AdvocateClerkListResponse
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkListResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("clerks")
          @Valid
                private List<AdvocateClerkSearchCriteria> clerks = null;

        @JsonProperty("pagination")

          @Valid
                private Pagination pagination = null;


        public AdvocateClerkListResponse addClerksItem(AdvocateClerkSearchCriteria clerksItem) {
            if (this.clerks == null) {
            this.clerks = new ArrayList<>();
            }
        this.clerks.add(clerksItem);
        return this;
        }

}
