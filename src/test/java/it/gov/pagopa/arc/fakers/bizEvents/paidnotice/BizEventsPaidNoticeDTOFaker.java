package it.gov.pagopa.arc.fakers.bizEvents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;

public class BizEventsPaidNoticeDTOFaker {
    public static BizEventsPaidNoticeDTO mockInstance(Integer bias, boolean isCart){
        return mockInstanceBuilder(bias, isCart).build();
    }
    public static BizEventsPaidNoticeDTO.BizEventsPaidNoticeDTOBuilder mockInstanceBuilder(Integer bias, boolean isCart) {
        if (!isCart) {
            return BizEventsPaidNoticeDTO
                    .builder()
                    .eventId("EVENT_ID%d" .formatted(bias))
                    .payeeName("PAYEE_NAME%d" .formatted(bias))
                    .payeeTaxCode("PAYEE_TAX_CODE%d" .formatted(bias))
                    .amount("2,681.52")
                    .noticeDate("2024-05-31T13:07:25Z")
                    .isCart(false)
                    .isPayer(true)
                    .isDebtor(true);
        } else {
            return BizEventsPaidNoticeDTO
                    .builder()
                    .eventId("EVENT_ID%d" .formatted(bias))
                    .payeeName("Pagamento Multiplo")
                    .payeeTaxCode("")
                    .noticeDate("2024-05-31T13:07:25Z")
                    .isCart(true)
                    .isPayer(false)
                    .isDebtor(true);
        }
    }
}
