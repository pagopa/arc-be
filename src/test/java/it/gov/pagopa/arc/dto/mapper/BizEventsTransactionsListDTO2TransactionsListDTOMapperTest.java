package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.fakers.TransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

class BizEventsTransactionsListDTO2TransactionsListDTOMapperTest {
    private BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapper;


    @BeforeEach
    void setUp() {
        transactionsListDTOMapper = new BizEventsTransactionsListDTO2TransactionsListDTOMapper();
    }
    @Test
    void givenApplyWhenBizEventsTransactionsListDTOThenReturnMappedDTO() {
        //given
        List<TransactionDTO> transactionsList = List.of(
                TransactionDTOFaker.mockInstance(1,false),
                TransactionDTOFaker.mockInstance(2,true));
        //when
        TransactionsListDTO mappedTransactionsListDTO = transactionsListDTOMapper.apply(transactionsList, 2);
        //then
        assertAll( () -> {
            Assertions.assertEquals(transactionsList, mappedTransactionsListDTO.getTransactions());
            Assertions.assertEquals(1, mappedTransactionsListDTO.getCurrentPage());
            Assertions.assertEquals(2, mappedTransactionsListDTO.getItemsForPage());
            Assertions.assertEquals(1, mappedTransactionsListDTO.getTotalPages());
            Assertions.assertEquals(10, mappedTransactionsListDTO.getTotalItems());
        });
    }
}