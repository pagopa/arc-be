package it.gov.pagopa.arc.connector.bizevents.dto.paidnotice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizEventsInfoPaidNoticeDTO {
    private String eventId;
    private String authCode;
    private String rrn;
    private String noticeDate;
    private String pspName;
    private BizEventsWalletInfoDTO walletInfo;
    private PaymentMethod paymentMethod;
    private BizEventsUserDetailDTO payer;
    private String amount;
    private String fee;
    private Origin origin;
}
