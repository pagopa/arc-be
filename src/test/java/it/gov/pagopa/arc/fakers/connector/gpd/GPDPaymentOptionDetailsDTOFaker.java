package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionMetadataDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentOptionDetailsStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GPDPaymentOptionDetailsDTOFaker {

    public static List<GPDPaymentOptionDetailsDTO> mockInstance(Integer bias, Boolean isPartialPayment) {
        List<GPDPaymentOptionDetailsDTO> gpdPaymentOptionDetailsDTOList = new ArrayList<>();

        gpdPaymentOptionDetailsDTOList.add(mockInstanceBuilder(bias, isPartialPayment).build());
        if (isPartialPayment){
            gpdPaymentOptionDetailsDTOList.add(mockInstanceBuilder(bias+1, true).build());
        }
        return gpdPaymentOptionDetailsDTOList;
    }

    public static GPDPaymentOptionDetailsDTO.GPDPaymentOptionDetailsDTOBuilder mockInstanceBuilder(Integer bias, Boolean isPartialPayment) {
        GPDPaymentOptionMetadataDTO gpdPaymentOptionMetadataDTO = GPDPaymentOptionMetadataDTOFaker.mockInstance(bias);
        GPDTransferDTO gpdTransferDTO = GPDTransferDTOFaker.mockInstance(bias);

        GPDPaymentOptionDetailsDTO.GPDPaymentOptionDetailsDTOBuilder paymentOptionDetailsDTO = getGpdPaymentOptionDetailsDTOBuilder(bias, gpdPaymentOptionMetadataDTO, gpdTransferDTO);

        if (isPartialPayment) {
            paymentOptionDetailsDTO
                    .isPartialPayment(true)
                    .description("Installments Payment");
        }

        return paymentOptionDetailsDTO;
    }

    private static GPDPaymentOptionDetailsDTO.GPDPaymentOptionDetailsDTOBuilder getGpdPaymentOptionDetailsDTOBuilder(Integer bias, GPDPaymentOptionMetadataDTO gpdPaymentOptionMetadataDTO, GPDTransferDTO gpdTransferDTO) {
        return GPDPaymentOptionDetailsDTO.builder()
                .nav("TEST_NAV%d".formatted(bias))
                .iuv("TEST_IUV%d".formatted(bias))
                .organizationFiscalCode("12345678901")
                .amount(1000L)
                .description("Single Payment")
                .isPartialPayment(false)
                .dueDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .retentionDate(LocalDateTime.parse("2024-11-01T23:59:59"))
                .paymentDate(null)
                .reportingDate(null)
                .insertedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .paymentMethod("creditCard")
                .fee(5L)
                .notificationFee(2L)
                .pspCompany("Test_PSP")
                .idReceipt("RECEIPT12345")
                .idFlowReporting("FLOW12345")
                .status(GPDPaymentOptionDetailsStatus.PO_UNPAID)
                .lastUpdatedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .paymentOptionMetadata(List.of(gpdPaymentOptionMetadataDTO))
                .transfer(List.of(gpdTransferDTO));
    }

}
