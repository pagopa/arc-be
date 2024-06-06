package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.utils.Utilities;
import org.springframework.stereotype.Service;
@Service
public class BizEventsTransactionDTO2TransactionDTO {
    public TransactionDTO apply(BizEventsTransactionDTO transaction){
        return TransactionDTO.builder()
                    .transactionId(transaction.getTransactionId())
                    .payeeName(transaction.getPayeeName())
                    .payeeTaxCode(transaction.getPayeeTaxCode())
                    .amount(transaction.getAmount() != null ? Utilities.euroToCents(transaction.getAmount()) : null)
                    .transactionDate(transaction.getTransactionDate() != null ? Utilities.dateStringToZonedDateTime(transaction.getTransactionDate()) : null)
                    .isCart(transaction.getIsCart())
                    .payedByMe(transaction.getIsPayer())
                    .registeredToMe(transaction.getIsDebtor())
                    .build();
    }
}