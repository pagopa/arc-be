package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDTO2NoticeDTOMapper;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeListDTO2NoticesListDTOMapper;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.paidnotice.BizEventsPaidNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class NoticesListResponseDTOMapperTest {
    @Mock
    private BizEventsPaidNoticeDTO2NoticeDTOMapper bizEventsPaidNoticeDTO2NoticeDTOMapperMock;
    @Mock
    private BizEventsPaidNoticeListDTO2NoticesListDTOMapper bizEventsPaidNoticeListDTO2NoticesListDTOMapperMock;

    NoticesListResponseDTOMapper mapper = Mappers.getMapper(NoticesListResponseDTOMapper.class);

    @Test
    void givenNoticesListDTOAndTokenWhenMapToFullResponseThenNoticesListResponseDTO() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        NoticeDTO noticeDTO2 = NoticeDTOFaker.mockInstance(2, false);
        List<NoticeDTO> noticeDTOList = List.of(noticeDTO, noticeDTO2);
        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(noticeDTOList).build();

        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTOFaker.mockInstance(1, false);
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO2 = BizEventsPaidNoticeDTOFaker.mockInstance(2, false);
        List<BizEventsPaidNoticeDTO> listOfBizEventsPaidNoticeDTO = List.of(bizEventsPaidNoticeDTO, bizEventsPaidNoticeDTO2);

        Mockito.when(bizEventsPaidNoticeDTO2NoticeDTOMapperMock.toNoticeDTOList(listOfBizEventsPaidNoticeDTO)).thenReturn(noticeDTOList);
        Mockito.when(bizEventsPaidNoticeListDTO2NoticesListDTOMapperMock.toNoticesListDTO(noticeDTOList)).thenReturn(noticesListDTO);
        //when
        NoticesListResponseDTO result = mapper.mapToFullResponse(
                bizEventsPaidNoticeDTO2NoticeDTOMapperMock,
                bizEventsPaidNoticeListDTO2NoticesListDTOMapperMock,
                listOfBizEventsPaidNoticeDTO,
                "continuationToken");
        //then
        TestUtils.assertNotNullFields(result);
        NoticeDTO noticeDTOResponse = result.getNoticesListDTO().getNotices().get(0);
        NoticeDTO noticeDTOResponse2 = result.getNoticesListDTO().getNotices().get(1);

        Assertions.assertEquals(noticeDTO,noticeDTOResponse);
        Assertions.assertEquals(noticeDTO2,noticeDTOResponse2);
        Assertions.assertEquals("continuationToken", result.getContinuationToken());

    }
}