package it.gov.pagopa.arc.fakers.bizEvents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDTO;

public class BizEventsTransactionDTOFaker {
    public static BizEventsTransactionDTO mockInstance(Integer bias, boolean isCart){
        return mockInstanceBuilder(bias, isCart).build();
    }
    public static BizEventsTransactionDTO.BizEventsTransactionDTOBuilder mockInstanceBuilder(Integer bias, boolean isCart) {
        if (!isCart) {
            return BizEventsTransactionDTO
                    .builder()
                    .transactionId("TRANSACTION_ID%d" .formatted(bias))
                    .payeeName("PAYEE_NAME%d" .formatted(bias))
                    .payeeTaxCode("PAYEE_TAX_CODE%d" .formatted(bias))
                    .amount("2.681,52")
                    .transactionDate("2024-05-31T13:07:25Z")
                    .isCart(false)
                    .isPayer(true)
                    .isDebtor(true);
        } else {
            return BizEventsTransactionDTO
                    .builder()
                    .transactionId("TRANSACTION_ID%d" .formatted(bias))
                    .payeeName("Pagamento Multiplo")
                    .payeeTaxCode("")
                    .transactionDate("2024-05-31T13:07:25Z")
                    .isCart(true)
                    .isPayer(false)
                    .isDebtor(true);
        }
    }
}
