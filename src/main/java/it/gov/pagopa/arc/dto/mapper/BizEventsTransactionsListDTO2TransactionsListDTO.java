package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizEventsTransactionsListDTO2TransactionsListDTO {
    public TransactionsListDTO apply(List<TransactionDTO> transactionsList, Integer size){
        return TransactionsListDTO.builder()
                .transactions(transactionsList)
                .currentPage(1)
                .itemsForPage(size)
                .totalItems(10)
                .totalPages(1)
                .build();
    }
}
