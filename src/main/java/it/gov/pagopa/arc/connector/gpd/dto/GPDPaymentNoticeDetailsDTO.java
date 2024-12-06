package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentNoticeDetailsStatus;
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
public class GPDPaymentNoticeDetailsDTO {
    private String iupd;

    private Boolean aca;

    private Boolean payStandIn;

    private String organizationFiscalCode;

    private GPDDebtorType type;

    private String companyName;

    private String officeName;

    private LocalDateTime insertedDate;

    private LocalDateTime publishDate;

    private LocalDateTime validityDate;

    private LocalDateTime paymentDate;

    private GPDPaymentNoticeDetailsStatus status;

    private LocalDateTime lastUpdatedDate;

    private List<GPDPaymentOptionDetailsDTO> paymentOption;

}
