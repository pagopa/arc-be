package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsInfoTransactionDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsInfoTransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.InfoTransactionDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
class BizEventsInfoTransaction2InfoTransactionDTOMapperTest {
    private BizEventsInfoTransaction2InfoTransactionDTOMapper transactionInfoMapper;
    @Mock
    private BizEventsWalletInfo2WalletInfoDTOMapper walletInfoMapperMock;
    @Mock
    private BizEventsUserDetail2UserDetailDTOMapper userDetailsMapperMock;

    @BeforeEach
    void setUp() {
        transactionInfoMapper = new BizEventsInfoTransaction2InfoTransactionDTOMapper(walletInfoMapperMock, userDetailsMapperMock);
    }

    @Test
    void givenBizEventsInfoTransactionDTOWhenMapInfoTransactionThenReturnInfoTransactionDTO() {
        //given
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        BizEventsUserDetailDTO payer = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        BizEventsInfoTransactionDTO bizEventsInfoTransaction = BizEventsInfoTransactionDTOFaker.mockInstance(bizEventsWalletInfo, payer);
        WalletInfoDTO walletInfo = CommonWalletInfoDTOFaker.mockWalletInfoDTO(false);
        UserDetailDTO payerMapped = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);

        Mockito.when(walletInfoMapperMock.mapWalletInfo(bizEventsWalletInfo)).thenReturn(walletInfo);
        Mockito.when(userDetailsMapperMock.mapUserDetail(any())).thenReturn(payerMapped);

        //when
        InfoTransactionDTO result = transactionInfoMapper.mapInfoTransaction(bizEventsInfoTransaction);
        //then
        commonAssert(result);

        assertEquals(walletInfo, result.getWalletInfo());
        assertEquals(InfoTransactionDTO.PaymentMethodEnum.PO, result.getPaymentMethod());
        assertEquals(payerMapped, result.getPayer());
        Mockito.verify(walletInfoMapperMock).mapWalletInfo(any());
        Mockito.verify(userDetailsMapperMock).mapUserDetail(any());
    }

    @Test
    void givenBizEventsInfoTransactionDTOWithoutPayerWhenMapInfoTransactionThenReturnInfoTransactionDTO() {
        //given
        BizEventsInfoTransactionDTO bizEventsInfoTransaction = BizEventsInfoTransactionDTO.builder()
                .transactionId("TRANSACTION_ID")
                .authCode("250863")
                .rrn("51561651")
                .transactionDate("2024-06-27T13:07:25Z")
                .pspName("Worldline Merchant Services Italia S.p.A.")
                .amount("5,654.3")
                .fee("0.29")
                .origin(Origin.PM)
                .build();

        //when
        InfoTransactionDTO result = transactionInfoMapper.mapInfoTransaction(bizEventsInfoTransaction);

        //then
        commonAssert(result);
    }

    private static void commonAssert(InfoTransactionDTO result) {
        assertNotNull(result);
        assertEquals("TRANSACTION_ID", result.getTransactionId());
        assertEquals("250863", result.getAuthCode());
        assertEquals("51561651", result.getRrn());
        assertEquals(ZonedDateTime.parse("2024-06-27T13:07:25Z"), result.getTransactionDate());
        assertEquals("Worldline Merchant Services Italia S.p.A.", result.getPspName());
        assertEquals( 565430L, result.getAmount());
        assertEquals( 29L, result.getFee());
        assertEquals(InfoTransactionDTO.OriginEnum.PM, result.getOrigin());
    }

}