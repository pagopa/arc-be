package it.gov.pagopa.arc.connector.bizevents.dto.paidnotice;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizEventsPaidNoticeDetailsDTO {

    private BizEventsInfoPaidNoticeDTO infoNotice;
    private List<BizEventsCartItemDTO> carts;
}
