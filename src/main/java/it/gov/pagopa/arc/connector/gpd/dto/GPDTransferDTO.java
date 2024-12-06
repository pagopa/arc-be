package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.gpd.enums.GPDTransferStatus;
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
public class GPDTransferDTO {

    private String organizationFiscalCode;

    private String companyName;

    private String idTransfer;

    private Long amount;

    private String remittanceInformation;

    private String category;

    private String iban;

    private String postalIban;

    private GPDTransferStampDTO stamp;

    private LocalDateTime insertedDate;

    private GPDTransferStatus status;

    private LocalDateTime lastUpdatedDate;

    private List<GPDPaymentOptionMetadataDTO> transferMetadata;
}
