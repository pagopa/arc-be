package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BizEventsPaidNoticeListDTO2NoticesListDTOTest {

    BizEventsPaidNoticeListDTO2NoticesListDTO mapper = Mappers.getMapper(BizEventsPaidNoticeListDTO2NoticesListDTO.class);
    @Test
    void givenListOfNoticeDTOWhenCallMapperThenReturnNoticesListDTO() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        NoticeDTO noticeDTO2 = NoticeDTOFaker.mockInstance(2, false);

        List<NoticeDTO> noticeList = List.of(noticeDTO, noticeDTO2);

        //when
        NoticesListDTO noticesListDTO = mapper.toNoticesListDTO(noticeList);
        //then
        commonAssert(noticeDTO, noticesListDTO.getNotices().get(0));
        commonAssert(noticeDTO2, noticesListDTO.getNotices().get(1));

        TestUtils.assertAllFieldsPopulated(noticesListDTO, List.of());
    }

    private static void commonAssert(NoticeDTO noticeDTO, NoticeDTO mappedDTO) {
        assertEquals(noticeDTO.getEventId(), mappedDTO.getEventId());
        assertEquals(noticeDTO.getPayeeName(), mappedDTO.getPayeeName());
        assertEquals(noticeDTO.getPayeeTaxCode(), mappedDTO.getPayeeTaxCode());
        assertEquals(noticeDTO.getAmount(),mappedDTO.getAmount());
        assertEquals(noticeDTO.getNoticeDate(), mappedDTO.getNoticeDate());
        assertEquals(noticeDTO.getIsCart(), mappedDTO.getIsCart());
        assertEquals(noticeDTO.getPaidByMe(), mappedDTO.getPaidByMe());
        assertEquals(noticeDTO.getRegisteredToMe(), mappedDTO.getRegisteredToMe());
    }
}