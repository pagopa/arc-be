package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsCartItem2CartItemDTOMapper;
import it.gov.pagopa.arc.fakers.NoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.paidnotice.BizEventsPaidNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(MockitoExtension.class)
class BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapperTest {
    @Mock
    private BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper bizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapperMock;
    @Mock
    private BizEventsCartItem2CartItemDTOMapper bizEventsCartItem2CartItemDTOMapperMock;
    @InjectMocks
    private final BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper mapper = Mappers.getMapper(BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper.class);

    @AfterEach
    void afterMethod() {
        Mockito.verifyNoMoreInteractions( bizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapperMock,bizEventsCartItem2CartItemDTOMapperMock);
    }

    @Test
    void givenBizEventsPaidNoticeDetailsDTOWhenCallToNoticeDetailsDTOThenReturnNoticeDetailsDTO() {
        //given
        BizEventsPaidNoticeDetailsDTO bizEventsPaidNoticeDetailsDTO = BizEventsPaidNoticeDetailsDTOFaker.mockInstance();

        NoticeDetailsDTO noticeDetailsDTO = NoticeDetailsDTOFaker.mockInstance();

        Mockito.when(bizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapperMock.toInfoNoticeDTO(bizEventsPaidNoticeDetailsDTO.getInfoNotice())).thenReturn(noticeDetailsDTO.getInfoNotice());
        Mockito.when(bizEventsCartItem2CartItemDTOMapperMock.mapCart( bizEventsPaidNoticeDetailsDTO.getCarts().get(0))).thenReturn(noticeDetailsDTO.getCarts().get(0));
        Mockito.when(bizEventsCartItem2CartItemDTOMapperMock.mapCart( bizEventsPaidNoticeDetailsDTO.getCarts().get(1))).thenReturn(noticeDetailsDTO.getCarts().get(1));
        //when
        NoticeDetailsDTO result = mapper.toNoticeDetailsDTO(bizEventsPaidNoticeDetailsDTO);
        //then
        assertNotNull(result);
        assertEquals(noticeDetailsDTO.getInfoNotice(), result.getInfoNotice());
        assertEquals(2, result.getCarts().size());
        assertEquals(noticeDetailsDTO.getCarts(), result.getCarts());

        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenBizEventsPaidNoticeDetailsDTOWithNullWhenCallToNoticeDetailsDTOThenReturnNoticeDetailsDTO() {
        //given
       BizEventsPaidNoticeDetailsDTO bizEventsPaidNoticeDetailsDTO = new BizEventsPaidNoticeDetailsDTO();
        //when
        NoticeDetailsDTO result = mapper.toNoticeDetailsDTO(bizEventsPaidNoticeDetailsDTO);
        //then
        Assertions.assertNull(result.getInfoNotice());
        Assertions.assertEquals(Collections.emptyList(), result.getCarts());

    }
}