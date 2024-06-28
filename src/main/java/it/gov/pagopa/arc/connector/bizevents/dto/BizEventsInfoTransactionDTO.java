package it.gov.pagopa.arc.connector.bizevents.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BizEventsInfoTransactionDTO {
    private String transactionId;
    private String authCode;
    private String rrn;
    private String transactionDate;
    private String pspName;
    @JsonProperty("walletInfo")
    private BizEventsWalletInfoDTO bizEventsWalletInfoDTO;
    private PaymentMethod paymentMethod;
    private BizEventsUserDetailDTO payer;
    private String amount;
    private String fee;
    private Origin origin;
}
