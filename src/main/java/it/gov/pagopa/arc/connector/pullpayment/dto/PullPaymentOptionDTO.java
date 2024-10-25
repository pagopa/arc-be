package it.gov.pagopa.arc.connector.pullpayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullPaymentOptionDTO {
    private String description;

    private Integer numberOfInstallments;

    private Long amount;

    private LocalDateTime dueDate;

    private Boolean isPartialPayment;

    private Boolean switchToExpired;

    private List<PullPaymentInstallmentDTO> installments;
}
