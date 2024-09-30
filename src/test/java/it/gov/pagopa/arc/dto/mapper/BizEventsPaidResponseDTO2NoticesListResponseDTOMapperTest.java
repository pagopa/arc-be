package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;


class BizEventsPaidResponseDTO2NoticesListResponseDTOMapperTest {

    BizEventsPaidResponseDTO2NoticesListResponseDTOMapper mapper = Mappers.getMapper(BizEventsPaidResponseDTO2NoticesListResponseDTOMapper.class);

    @Test
    void givenToNoticesListResponseDTOWhenThen() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        NoticeDTO noticeDTO2 = NoticeDTOFaker.mockInstance(2, false);
        List<NoticeDTO> noticeDTOList = List.of(noticeDTO, noticeDTO2);
        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(noticeDTOList).build();

        //when
        NoticesListResponseDTO noticesListResponseDTO = mapper.toNoticesListResponseDTO(noticesListDTO, "continuationToken");
        //then
        TestUtils.assertNotNullFields(noticesListResponseDTO);
        NoticeDTO noticeDTOResponse = noticesListResponseDTO.getNoticesListDTO().getNotices().get(0);
        NoticeDTO noticeDTOResponse2 = noticesListResponseDTO.getNoticesListDTO().getNotices().get(1);

        Assertions.assertEquals(noticeDTO,noticeDTOResponse);
        Assertions.assertEquals(noticeDTO2,noticeDTOResponse2);
        Assertions.assertEquals("continuationToken", noticesListResponseDTO.getContinuationToken());

    }
}