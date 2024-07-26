package it.gov.pagopa.arc.fakers.paymentNotices;

import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import it.gov.pagopa.arc.model.generated.PaymentOptionStatus;

import java.time.LocalDateTime;

public class InstallmentDTOFaker {
    public static InstallmentDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static InstallmentDTO.InstallmentDTOBuilder mockInstanceBuilder(){
        return InstallmentDTO.builder()
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
                .status(PaymentOptionStatus.UNPAID)
                .lastUpdatedDate(LocalDateTime.parse("2024-04-11T06:56:14.845126"));
    }
}
