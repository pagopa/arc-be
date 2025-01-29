package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GPDPaymentOptionPayloadDTO {

    private String nav;

    @NotBlank
    private String iuv;

    @NotNull
    private Long amount;

    @NotBlank
    private String description;

    @NotNull
    private Boolean isPartialPayment;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime retentionDate;

    private Long fee;

    private Long notificationFee;

    @Valid
    private List<GPDTransferPayloadDTO> transfer;

    private List<GPDPaymentOptionMetadataDTO> paymentOptionMetadata;

}
