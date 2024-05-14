package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static utils.TestUtils.objectMapper;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    @Autowired
    private TransactionsService transactionsService;
    private static final String FISCAL_CODE = "SPCPQL80L16F800B";

    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsServiceImpl(objectMapper);
    }

    @Test
    void givenFiscalCodeWhenCallRetrieveTransactionsListThenReturnTransactionList() {
        //given
        //when
        List<TransactionDTO> transactionDTOList = transactionsService.retrieveTransactionsList(FISCAL_CODE);

        //then
        Assertions.assertNotNull(transactionDTOList);
        Assertions.assertEquals("1", transactionDTOList.get(0).getTransactionId());
        Assertions.assertEquals(ZonedDateTime.parse("2024-03-27T13:07:25Z"), transactionDTOList.get(0).getTransactionDate());
        Assertions.assertEquals("MI_XXX",transactionDTOList.get(0).getPayeeTaxCode());
        Assertions.assertEquals("180,00", transactionDTOList.get(0).getAmount());
        Assertions.assertFalse(transactionDTOList.get(0).getIsCart());
        Assertions.assertTrue(transactionDTOList.get(0).getPayedByMe());
        Assertions.assertTrue(transactionDTOList.get(0).getRegisteredToMe());
        Assertions.assertEquals(2,transactionDTOList.size());

    }
}