package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.TransactionDTO;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TransactionDTOFaker {
    public static TransactionDTO mockInstance(Integer bias, boolean isCart){
        return mockInstanceBuilder(bias, isCart).build();
    }
    public static TransactionDTO.TransactionDTOBuilder mockInstanceBuilder(Integer bias, boolean isCart) {
        if (!isCart) {
            return TransactionDTO
                    .builder()
                    .transactionId("TRANSACTION_ID%d" .formatted(bias))
                    .payeeName("PAYEE_NAME%d" .formatted(bias))
                    .payeeTaxCode("PAYEE_TAX_CODE%d" .formatted(bias))
                    .amount(268152L)
                    .transactionDate(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                    .isCart(false)
                    .paidByMe(true)
                    .registeredToMe(true);
        } else {
            return TransactionDTO
                    .builder()
                    .transactionId("TRANSACTION_ID%d" .formatted(bias))
                    .payeeName("Pagamento Multiplo")
                    .payeeTaxCode("")
                    .transactionDate(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                    .isCart(true)
                    .paidByMe(false)
                    .registeredToMe(true);
        }
    }
}
