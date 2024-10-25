package it.gov.pagopa.arc.connector.pullpayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentOptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullPaymentInstallmentDTO {
    private String nav;

    private String iuv;

    private String paTaxCode;

    private String paFullName;

    private Long amount;

    private String description;

    private LocalDateTime dueDate;

    private LocalDateTime retentionDate;

    private LocalDateTime insertedDate;

    private Long notificationFee;

    private PullPaymentOptionStatus status;

    private LocalDateTime lastUpdatedDate;
}
