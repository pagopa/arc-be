package it.gov.pagopa.arc.fakers.connector.pullPayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentOptionStatus;

import java.time.LocalDateTime;

public class PullPaymentInstallmentDTOFaker {

    public static PullPaymentInstallmentDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static PullPaymentInstallmentDTO.PullPaymentInstallmentDTOBuilder mockInstanceBuilder(){
        return PullPaymentInstallmentDTO.builder()
                .nav("347000000880099993")
                .iuv("47000000880099993")
                .paTaxCode("99999000013")
                .paFullName("EC Demo Pagamenti Pull Test")
                .amount(120L)
                .description("Test Pull - unica opzione")
                .dueDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .retentionDate(LocalDateTime.parse("2024-11-30T23:59:59"))
                .insertedDate(LocalDateTime.parse("2024-04-11T06:56:14.845126"))
                .notificationFee(0L)
                .status(PullPaymentOptionStatus.PO_UNPAID)
                .lastUpdatedDate(LocalDateTime.parse("2024-04-11T06:56:14.845126"));
    }
}
