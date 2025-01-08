package it.gov.pagopa.arc.fakers.paymentNotices;

import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionDTO;

import java.util.List;

public class PaymentNoticeDTOFaker {
    public static PaymentNoticeDTO mockInstance(Boolean singlePaymentOption){
        return mockInstanceBuilder(singlePaymentOption).build();
    }

    public static PaymentNoticeDTO.PaymentNoticeDTOBuilder mockInstanceBuilder(Boolean singlePaymentOption){
        InstallmentDTO installment = InstallmentDTOFaker.mockInstance();

        PaymentNoticeDTO.PaymentNoticeDTOBuilder paymentNoticeDTO = PaymentNoticeDTO.builder()
                .iupd("99999000013-64c8e41bfec846e99c92fc0fe5899993")
                .paTaxCode("99999000013")
                .paFullName("EC Demo Pagamenti Pull Test")
                .status(PaymentNoticeStatus.VALID);

        if(!singlePaymentOption){
            PaymentOptionDTO option = PaymentOptionDTOFaker.mockInstance(installment, false);
            PaymentOptionDTO optionInstallments = PaymentOptionDTOFaker.mockInstance(installment, true);
            paymentNoticeDTO.paymentOptions(List.of(option,optionInstallments));
        }else {
            PaymentOptionDTO option = PaymentOptionDTOFaker.mockInstance(installment, false);
            paymentNoticeDTO.paymentOptions(List.of(option));
        }

        return paymentNoticeDTO;
    }
}
