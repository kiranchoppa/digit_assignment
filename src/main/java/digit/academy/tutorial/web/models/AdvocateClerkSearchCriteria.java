package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.AdvocateClerk;
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
 * the fields specified will be used in a logical AND condition
 */
@Schema(description = "the fields specified will be used in a logical AND condition")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2026-06-15T17:43:10.444664432+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkSearchCriteria   {
        @JsonProperty("id")

                private String id = null;

        @JsonProperty("applicationNumber")

                private String applicationNumber = null;

        @JsonProperty("stateRegnNumber")

                private String stateRegnNumber = null;

        @JsonProperty("individualId")

                private String individualId = null;

        @JsonProperty("responseList")
          @Valid
                private List<AdvocateClerk> responseList = null;


        public AdvocateClerkSearchCriteria addResponseListItem(AdvocateClerk responseListItem) {
            if (this.responseList == null) {
            this.responseList = new ArrayList<>();
            }
        this.responseList.add(responseListItem);
        return this;
        }

}
