package it.gov.pagopa.arc.connector.gpd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentNoticeStatus;
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
public class GPDPaymentNoticePayloadDTO {

    @NotBlank
    private String iupd;

    @NotBlank
    private GPDDebtorType type;

    @Builder.Default
    private Boolean payStandIn = true;

    @NotBlank
    private String fiscalCode;

    @NotBlank
    private String fullName;

    private String streetName;

    private String civicNumber;

    private String postalCode;

    private String city;

    private String province;

    private String region;

    private String country;

    private String email;

    private String phone;

    @NotNull
    @Builder.Default
    private Boolean switchToExpired = true;

    @NotBlank
    private String companyName;

    private String officeName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime validityDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime paymentDate;

    private GPDPaymentNoticeStatus status;

    @Valid
    private List<GPDPaymentOptionPayloadDTO> paymentOption;
}
