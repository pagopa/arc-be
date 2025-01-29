package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GPDTransferPayloadDTO {

    @NotBlank
    private String idTransfer;

    @NotNull
    private Long amount;

    private String organizationFiscalCode;

    @NotBlank
    private String remittanceInformation;

    @NotBlank
    private String category;

    private String iban;

    private String postalIban;

    private GPDTransferStampDTO stamp;

    private String companyName;

    private List<GPDPaymentOptionMetadataDTO> transferMetadata;


}
