package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;

public class PaymentNoticePayloadDTOFaker {

    public static PaymentNoticePayloadDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static PaymentNoticePayloadDTO.PaymentNoticePayloadDTOBuilder mockInstanceBuilder(){
        return PaymentNoticePayloadDTO.builder()
                .paTaxCode("ORGANIZATION_FISCAL_CODE")
                .paFullName("ORGANIZATION_NAME")
                .amount(120L)
                .description("Test description");
    }
}
