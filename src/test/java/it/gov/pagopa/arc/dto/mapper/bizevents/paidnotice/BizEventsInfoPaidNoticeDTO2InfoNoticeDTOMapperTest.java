package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsInfoPaidNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsUserDetail2UserDetailDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsWalletInfo2WalletInfoDTOMapper;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.paidnotice.BizEventsInfoPaidNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.InfoNoticeDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapperTest {
    @Mock
    private BizEventsUserDetail2UserDetailDTOMapper bizEventsUserDetail2UserDetailDTOMapperMock;
    @Mock
    private BizEventsWalletInfo2WalletInfoDTOMapper bizEventsWalletInfo2WalletInfoDTOMapperMock;

    @InjectMocks
    private final BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper mapper = Mappers.getMapper(BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper.class);

    @AfterEach
    void afterMethod() {
        Mockito.verifyNoMoreInteractions( bizEventsUserDetail2UserDetailDTOMapperMock, bizEventsWalletInfo2WalletInfoDTOMapperMock);
    }
    @Test
    void givenBizEventsInfoPaidNoticeDTOWhenCallMapperThenReturnInfoNoticeDTO() {
        //given
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        BizEventsUserDetailDTO payer = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        BizEventsInfoPaidNoticeDTO bizEventsInfoPaidNoticeDTO = BizEventsInfoPaidNoticeDTOFaker.mockInstance(bizEventsWalletInfo, payer);

        WalletInfoDTO walletInfoDTO = CommonWalletInfoDTOFaker.mockWalletInfoDTO(false);
        UserDetailDTO userDetailDTO = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);

        Mockito.when(bizEventsWalletInfo2WalletInfoDTOMapperMock.mapWalletInfo(bizEventsWalletInfo)).thenReturn(walletInfoDTO);
        Mockito.when(bizEventsUserDetail2UserDetailDTOMapperMock.mapUserDetail(payer)).thenReturn(userDetailDTO);
        //when
        InfoNoticeDTO infoNoticeDTO = mapper.toInfoNoticeDTO(bizEventsInfoPaidNoticeDTO);
        //then
        Assertions.assertEquals("EVENT_ID", infoNoticeDTO.getEventId());
        Assertions.assertEquals("250863", infoNoticeDTO.getAuthCode());
        Assertions.assertEquals("51561651", infoNoticeDTO.getRrn());
        Assertions.assertEquals("51561651", infoNoticeDTO.getRrn());
        Assertions.assertEquals(ZonedDateTime.parse("2024-06-27T13:07:25Z"), infoNoticeDTO.getNoticeDate());
        Assertions.assertEquals("Worldline Merchant Services Italia S.p.A.", infoNoticeDTO.getPspName());
        assertEquals( 565430L, infoNoticeDTO.getAmount());
        assertEquals( 29L, infoNoticeDTO.getFee());
        assertEquals(InfoNoticeDTO.OriginEnum.PM, infoNoticeDTO.getOrigin());
    }
}