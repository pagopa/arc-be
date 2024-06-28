package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.*;
import it.gov.pagopa.arc.fakers.CartItemDTOFaker;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.fakers.InfoTransactionDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsCartItemDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsInfoTransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        BizEventsUserDetailDTO payer = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        BizEventsInfoTransactionDTO bizEventsInfoTransaction = BizEventsInfoTransactionDTOFaker.mockInstance(bizEventsWalletInfo, payer);

        BizEventsUserDetailDTO payee = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        BizEventsUserDetailDTO debtor = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);
        BizEventsCartItemDTO bizEventsCartItem1 = BizEventsCartItemDTOFaker.mockInstance(payee,debtor);
        BizEventsCartItemDTO bizEventsCartItem2 = BizEventsCartItemDTOFaker.mockInstance(payee,debtor);
        List<BizEventsCartItemDTO> bizEventsCartsList = List.of(bizEventsCartItem1, bizEventsCartItem2);

        BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO = BizEventsTransactionDetailsDTO.builder()
                .bizEventsInfoTransactionDTO(bizEventsInfoTransaction)
                .bizEventsCartsDTO(bizEventsCartsList)
                .build();


        //create TransactionDetailsDTO
        WalletInfoDTO walletInfo = CommonWalletInfoDTOFaker.mockWalletInfoDTO(false);
        UserDetailDTO payerMapped = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        InfoTransactionDTO infoTransaction = InfoTransactionDTOFaker.mockInstance(walletInfo, payerMapped);

        UserDetailDTO payeeResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        UserDetailDTO debtorResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);
        CartItemDTO cartItem1 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        CartItemDTO cartItem2 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        List<CartItemDTO> cartsList = List.of(cartItem1, cartItem2);

        Mockito.when(transactionInfoMapperMock.mapInfoTransaction(bizEventsInfoTransaction)).thenReturn(infoTransaction);
        //when
        TransactionDetailsDTO result = transactionDetailsMapper.apply(bizEventsTransactionDetailsDTO, cartsList);
        //then
        assertNotNull(result);
        assertEquals(infoTransaction, result.getInfoTransaction());
        System.out.println(infoTransaction.getWalletInfo());
        System.out.println(result.getInfoTransaction().getWalletInfo());
        assertEquals(2, result.getCarts().size());
        assertEquals( cartsList, result.getCarts());
        Mockito.verify(transactionInfoMapperMock).mapInfoTransaction(any());
    }
}