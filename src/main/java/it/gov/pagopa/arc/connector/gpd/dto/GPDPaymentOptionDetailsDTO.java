package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentOptionDetailsStatus;
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
public class GPDPaymentOptionDetailsDTO {

    private String nav;

    private String iuv;

    private String organizationFiscalCode;

    private Long amount;

    private String description;

    private Boolean isPartialPayment;

    private LocalDateTime dueDate;

    private LocalDateTime retentionDate;

    private LocalDateTime paymentDate;

    private LocalDateTime reportingDate;

    private LocalDateTime insertedDate;

    private String paymentMethod;

    private Long fee;

    private Long notificationFee;

    private String pspCompany;

    private String idReceipt;

    private String idFlowReporting;

    private GPDPaymentOptionDetailsStatus status;

    private LocalDateTime lastUpdatedDate;

    private List<GPDPaymentOptionMetadataDTO> paymentOptionMetadata;

    private List<GPDTransferDTO> transfer;

}
