package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDTO;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsTransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.utils.Utilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BizEventsTransactionDTO2TransactionDTOTest {

    private BizEventsTransactionDTO2TransactionDTO transactionDTOMapper;

    @BeforeEach
    void setUp() {
        transactionDTOMapper = new BizEventsTransactionDTO2TransactionDTO();
    }

    @Test
    void givenApplyWhenBizEventsTransactionDTOIsCartFalseThenReturnMappedDTO() {
        //given
        BizEventsTransactionDTO bizEventsTransaction = BizEventsTransactionDTOFaker.mockInstance(1,false);
        //when
        TransactionDTO dtoMapped = transactionDTOMapper.apply(bizEventsTransaction);

        //then
        commonAssert(bizEventsTransaction, dtoMapped);
        assertEquals(Utilities.euroToCents(bizEventsTransaction.getAmount()), dtoMapped.getAmount());
    }

    @Test
    void givenApplyWhenBizEventsTransactionDTOIsCartTrueThenReturnMappedDTO() {
        //given
        BizEventsTransactionDTO bizEventsTransaction = BizEventsTransactionDTOFaker.mockInstance(1,true);
        //when
        TransactionDTO dtoMapped = transactionDTOMapper.apply(bizEventsTransaction);

        //then
        commonAssert(bizEventsTransaction, dtoMapped);
        assertNull(dtoMapped.getAmount());
    }

    private static void commonAssert(BizEventsTransactionDTO bizEventsTransaction, TransactionDTO dtoMapped) {
        assertAll( () -> {
            assertEquals(bizEventsTransaction.getTransactionId(), dtoMapped.getTransactionId());
            assertEquals(bizEventsTransaction.getPayeeName(), dtoMapped.getPayeeName());
            assertEquals(bizEventsTransaction.getPayeeTaxCode(), dtoMapped.getPayeeTaxCode());
            assertEquals(Utilities.dateStringToZonedDateTime(bizEventsTransaction.getTransactionDate()), dtoMapped.getTransactionDate());
            assertEquals(bizEventsTransaction.getIsCart(), dtoMapped.getIsCart());
            assertEquals(bizEventsTransaction.getIsPayer(), dtoMapped.getPayedByMe());
            assertEquals(bizEventsTransaction.getIsDebtor(), dtoMapped.getRegisteredToMe());
        });
    }
}