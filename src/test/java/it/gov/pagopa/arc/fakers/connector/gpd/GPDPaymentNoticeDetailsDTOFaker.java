package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentNoticeStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GPDPaymentNoticeDetailsDTOFaker {
    public static GPDPaymentNoticeDetailsDTO mockInstance(Integer bias, Boolean isPartialPayment) {
        return mockInstanceBuilder(bias, isPartialPayment).build();
    }

    public static GPDPaymentNoticeDetailsDTO.GPDPaymentNoticeDetailsDTOBuilder mockInstanceBuilder(Integer bias, Boolean isPartialPayment) {
        List<GPDPaymentOptionDetailsDTO> gpdPaymentOptionDetailsDTOList = GPDPaymentOptionDetailsDTOFaker.mockInstance(bias, isPartialPayment);

        return GPDPaymentNoticeDetailsDTO.builder()
                .iupd("TEST_IUPD%d".formatted(bias))
                .aca(false)
                .payStandIn(true)
                .organizationFiscalCode("12345678901")
                .type(GPDDebtorType.F)
                .companyName("Test Company")
                .officeName("Test Office")
                .insertedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .publishDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .validityDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .paymentDate(null)
                .status(GPDPaymentNoticeStatus.VALID)
                .lastUpdatedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .paymentOption(gpdPaymentOptionDetailsDTOList);
    }
}
