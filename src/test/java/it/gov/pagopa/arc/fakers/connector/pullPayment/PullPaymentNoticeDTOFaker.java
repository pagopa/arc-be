package it.gov.pagopa.arc.fakers.connector.pullPayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentNoticeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PullPaymentNoticeDTOFaker {
    public static PullPaymentNoticeDTO mockInstance(Boolean singlePaymentOption){
        return mockInstanceBuilder(singlePaymentOption).build();
    }

    public static PullPaymentNoticeDTO.PullPaymentNoticeDTOBuilder mockInstanceBuilder(Boolean singlePaymentOption){
        PullPaymentInstallmentDTO installment = PullPaymentInstallmentDTOFaker.mockInstance();

        PullPaymentNoticeDTO.PullPaymentNoticeDTOBuilder pullPaymentNoticeDTO = PullPaymentNoticeDTO.builder()
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
                .lastUpdateDate(LocalDate.parse("2024-04-11"));

        if(!singlePaymentOption){
            PullPaymentOptionDTO option = PullPaymentOptionDTOFaker.mockInstance(installment, false);
            PullPaymentOptionDTO optionInstallments = PullPaymentOptionDTOFaker.mockInstance(installment, true);
            pullPaymentNoticeDTO.paymentOptions(List.of(option,optionInstallments));
        }else {
            PullPaymentOptionDTO option = PullPaymentOptionDTOFaker.mockInstance(installment, false);
            pullPaymentNoticeDTO.paymentOptions(List.of(option));
        }

        return pullPaymentNoticeDTO;
    }

}
