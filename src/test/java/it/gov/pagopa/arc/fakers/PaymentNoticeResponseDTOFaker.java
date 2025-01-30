package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.PaymentNoticeResponseDTO;

public class PaymentNoticeResponseDTOFaker {
    public static PaymentNoticeResponseDTO mockInstance(){
        return mockInstanceBuilder().build();
    }
    
    public static PaymentNoticeResponseDTO.PaymentNoticeResponseDTOBuilder mockInstanceBuilder(){
        return PaymentNoticeResponseDTO.builder()
                .nav("302040501822520951")
                .paTaxCode("ORGANIZATION_FISCAL_CODE")
                .paFullName("ORGANIZATION_NAME")
                .amount(120L)
                .description("Test description");
    }
}
