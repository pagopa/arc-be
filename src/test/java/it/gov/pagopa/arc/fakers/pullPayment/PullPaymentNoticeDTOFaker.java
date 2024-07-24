package it.gov.pagopa.arc.fakers.pullPayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentNoticeStatus;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentOptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PullPaymentNoticeDTOFaker {
    public static PullPaymentNoticeDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static PullPaymentNoticeDTO.PullPaymentNoticeDTOBuilder mockInstanceBuilder(){
        PullPaymentInstallmentDTO installment = PullPaymentInstallmentDTO.builder()
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
                .lastUpdatedDate(LocalDateTime.parse("2024-04-11T06:56:14.845126"))
                .build();

        PullPaymentOptionDTO option =  PullPaymentOptionDTO.builder()
                .description("Test Pull - unica opzione")
                .numberOfInstallments(1)
                .amount(120L)
                .dueDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .isPartialPayment(false)
                .switchToExpired(false)
                .installments(List.of(installment))
                .build();

        return PullPaymentNoticeDTO.builder()
                .iupd("99999000013-64c8e41bfec846e99c92fc0fe5899993")
                .debtorTaxCode("STCCST83A15L1131")
                .debtorFullName("EC Demo Pagamenti Pull Test")
                .debtorType("F")
                .paTaxCode("99999000013")
                .paFullName("EC Demo Pagamenti Pull Test")
                .insertedDate(LocalDateTime.parse("2024-04-11T06:56:14.845126"))
                .publishDate(LocalDateTime.parse("2024-04-11T06:56:14.845142"))
                .validityDate(LocalDateTime.parse("2024-04-11T06:56:14.845142"))
                .status(PullPaymentNoticeStatus.VALID)
                .lastUpdateDate(LocalDate.parse("2024-04-11"))
                .paymentOptions(List.of(option));
    }

}
