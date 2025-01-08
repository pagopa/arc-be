package it.gov.pagopa.arc.fakers.connector;

import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionDetailsDTO;

import java.util.List;

public class PaymentNoticeDetailsDTOFaker {
    public static PaymentNoticeDetailsDTO mockInstance(Integer bias, Boolean isPartialPayment) {
        return mockInstanceBuilder(bias, isPartialPayment).build();
    }

    public static PaymentNoticeDetailsDTO.PaymentNoticeDetailsDTOBuilder mockInstanceBuilder(Integer bias, Boolean isPartialPayment) {
        List<PaymentOptionDetailsDTO> paymentOptionDetailsDTOList = PaymentOptionDetailsDTOFaker.mockInstance(bias, isPartialPayment);

        return PaymentNoticeDetailsDTO.builder()
                .iupd("TEST_IUPD%d".formatted(bias))
                .paTaxCode("12345678901")
                .paFullName("Test Company")
                .status(PaymentNoticeDetailsStatus.VALID)
                .paymentOptions(paymentOptionDetailsDTOList);
    }
}
