package it.gov.pagopa.arc.fakers.connector.pullPayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;

import java.time.LocalDateTime;
import java.util.List;

public class PullPaymentOptionDTOFaker {
    public static PullPaymentOptionDTO mockInstance(PullPaymentInstallmentDTO installmentDTO, Boolean paymentInstallments){
        return mockInstanceBuilder(installmentDTO, paymentInstallments).build();
    }
    public static PullPaymentOptionDTO.PullPaymentOptionDTOBuilder mockInstanceBuilder(PullPaymentInstallmentDTO installment,  Boolean paymentInstallments){
        PullPaymentOptionDTO.PullPaymentOptionDTOBuilder pullPaymentOptionDTO = PullPaymentOptionDTO.builder()
                .description("Test Pull - unica opzione")
                .numberOfInstallments(1)
                .amount(120L)
                .dueDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .isPartialPayment(false)
                .switchToExpired(false)
                .installments(List.of(installment));

        if(paymentInstallments){
            pullPaymentOptionDTO.description("Test Pull - piano rateale");
            installment.setDescription("Test Pull - piano rateale");
            pullPaymentOptionDTO.numberOfInstallments(2).installments(List.of(installment,installment));
        }

        return pullPaymentOptionDTO;
    }
}
