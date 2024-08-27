package it.gov.pagopa.arc.connector.pullpayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentNoticeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullPaymentNoticeDTO {

    private String iupd;

    private String debtorTaxCode;

    private String debtorFullName;

    private String debtorType;

    private String paTaxCode;

    private String paFullName;

    private LocalDateTime insertedDate;

    private LocalDateTime publishDate;

    private LocalDateTime validityDate;

    private PullPaymentNoticeStatus status;

    private LocalDate lastUpdateDate;

    private List<PullPaymentOptionDTO> paymentOptions;

}
