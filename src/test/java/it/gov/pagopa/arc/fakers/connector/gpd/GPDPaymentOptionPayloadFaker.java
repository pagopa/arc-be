package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;

import java.time.LocalDateTime;
import java.util.List;

public class GPDPaymentOptionPayloadFaker {
    public static GPDPaymentOptionPayloadDTO mockInstance(String organizationFiscalCode) {
        return mockInstanceBuilder(organizationFiscalCode).build();
    }
    public static GPDPaymentOptionPayloadDTO.GPDPaymentOptionPayloadDTOBuilder mockInstanceBuilder(String organizationFiscalCode) {
        GPDTransferPayloadDTO gpdTransferPayloadDTO = GPDTransferPayloadFaker.mockInstance(organizationFiscalCode);
        return GPDPaymentOptionPayloadDTO.builder()
                        .iuv("02040501822520951")
                        .amount(966L)
                        .description("Test Pull - unica opzione")
                        .isPartialPayment(false)
                        .dueDate(LocalDateTime.parse("2025-10-30T23:59:59"))
                        .retentionDate(LocalDateTime.parse("2025-11-30T23:59:59"))
                        .transfer(List.of(gpdTransferPayloadDTO));
        }

}
