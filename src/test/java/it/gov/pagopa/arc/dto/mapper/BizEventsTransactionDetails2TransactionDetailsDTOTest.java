package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.fakers.TransactionDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsTransactionDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BizEventsTransactionDetails2TransactionDetailsDTOTest {

    private BizEventsTransactionDetails2TransactionDetailsDTO transactionDetailsMapper;

    @Mock
    private BizEventsInfoTransaction2InfoTransactionDTO transactionInfoMapperMock;

    @BeforeEach
    void setUp() {
        transactionDetailsMapper = new BizEventsTransactionDetails2TransactionDetailsDTO(transactionInfoMapperMock);
    }

    @Test
    void givenBizEventsTransactionDetailsWhenApplyThenReturnTransactionDetails() {
        //given
        //create BizEventsTransactionDetailsDTO
        BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO = BizEventsTransactionDetailsDTOFaker.mockInstance();

        //create TransactionDetailsDTO
        TransactionDetailsDTO transactionDetailsDTO = TransactionDetailsDTOFaker.mockInstance();

        Mockito.when(transactionInfoMapperMock.mapInfoTransaction(bizEventsTransactionDetailsDTO.getBizEventsInfoTransactionDTO())).thenReturn(transactionDetailsDTO.getInfoTransaction());
        //when
        TransactionDetailsDTO result = transactionDetailsMapper.apply(bizEventsTransactionDetailsDTO, transactionDetailsDTO.getCarts());
        //then
        assertNotNull(result);
        assertEquals(transactionDetailsDTO.getInfoTransaction(), result.getInfoTransaction());
        assertEquals(2, result.getCarts().size());
        assertEquals( transactionDetailsDTO.getCarts(), result.getCarts());
        Mockito.verify(transactionInfoMapperMock).mapInfoTransaction(any());
    }
}