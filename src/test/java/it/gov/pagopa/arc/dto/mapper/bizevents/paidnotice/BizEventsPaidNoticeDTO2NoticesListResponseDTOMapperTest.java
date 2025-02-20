package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.fakers.bizEvents.paidnotice.BizEventsPaidNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BizEventsPaidNoticeDTO2NoticesListResponseDTOMapperTest {
    private final BizEventsPaidNoticeDTO2NoticesListResponseDTOMapper mapper = Mappers.getMapper(BizEventsPaidNoticeDTO2NoticesListResponseDTOMapper.class);
    @Test
    void givenBizEventsPaidNoticeWhenCallMapperThenReturnNoticeDTO() {
        //given
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTOFaker.mockInstance(1, false);
        //when
        NoticeDTO dtoMapped = mapper.toNoticeDTO(bizEventsPaidNoticeDTO);
        //then
        commonAssert(bizEventsPaidNoticeDTO, dtoMapped);
        assertEquals(MapperUtilities.euroToCents(bizEventsPaidNoticeDTO.getAmount()), dtoMapped.getAmount());
    }
    @Test
    void givenBizEventsPaidNoticeWhenIsCartIsTrueThenReturnNoticeDTO(){
        //given
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTOFaker.mockInstance(1, true);
        //when
        NoticeDTO dtoMapped = mapper.toNoticeDTO(bizEventsPaidNoticeDTO);

        //then
        commonAssert(bizEventsPaidNoticeDTO, dtoMapped);
        assertNull(dtoMapped.getAmount());
    }

    @Test
    void givenBizEventsPaidNoticeWhenAmountAndDateAreNullThenReturnNoticeDTO(){
        //given
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTOFaker.mockInstance(1, false);
        bizEventsPaidNoticeDTO.setAmount(null);
        bizEventsPaidNoticeDTO.setNoticeDate(null);
        //when
        NoticeDTO dtoMapped = mapper.toNoticeDTO(bizEventsPaidNoticeDTO);

        //then
        assertEquals(bizEventsPaidNoticeDTO.getEventId(), dtoMapped.getEventId());
        assertEquals(bizEventsPaidNoticeDTO.getPayeeName(), dtoMapped.getPayeeName());
        assertEquals(bizEventsPaidNoticeDTO.getPayeeTaxCode(), dtoMapped.getPayeeTaxCode());
        assertEquals(bizEventsPaidNoticeDTO.getIsCart(), dtoMapped.getIsCart());
        assertEquals(bizEventsPaidNoticeDTO.getIsPayer(), dtoMapped.getPaidByMe());
        assertEquals(bizEventsPaidNoticeDTO.getIsDebtor(), dtoMapped.getRegisteredToMe());
        assertNull(dtoMapped.getAmount());
        assertNull(dtoMapped.getNoticeDate());
    }

    @Test
    void givenBizEventsPaidNoticeListDTOWhenCallMapperThenReturnListOfNoticeDTO() {
        //given
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTOFaker.mockInstance(1, false);
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO2 = BizEventsPaidNoticeDTOFaker.mockInstance(2, false);
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = BizEventsPaidNoticeListDTO.builder().notices(List.of(bizEventsPaidNoticeDTO, bizEventsPaidNoticeDTO2)).build();
        //when
        NoticesListDTO result = mapper.toNoticeListDTO(bizEventsPaidNoticeListDTO);
        //then
        commonAssert(bizEventsPaidNoticeDTO, result.getNotices().getFirst());
        commonAssert(bizEventsPaidNoticeDTO2, result.getNotices().get(1));
        assertEquals(MapperUtilities.euroToCents(bizEventsPaidNoticeDTO.getAmount()), result.getNotices().getFirst().getAmount());
        assertEquals(MapperUtilities.euroToCents(bizEventsPaidNoticeDTO2.getAmount()), result.getNotices().get(1).getAmount());

    }

    private static void commonAssert(BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO, NoticeDTO dtoMapped) {
            assertEquals(bizEventsPaidNoticeDTO.getEventId(), dtoMapped.getEventId());
            assertEquals(bizEventsPaidNoticeDTO.getPayeeName(), dtoMapped.getPayeeName());
            assertEquals(bizEventsPaidNoticeDTO.getPayeeTaxCode(), dtoMapped.getPayeeTaxCode());
            assertEquals(MapperUtilities.dateStringToZonedDateTime(bizEventsPaidNoticeDTO.getNoticeDate()), dtoMapped.getNoticeDate());
            assertEquals(bizEventsPaidNoticeDTO.getIsCart(), dtoMapped.getIsCart());
            assertEquals(bizEventsPaidNoticeDTO.getIsPayer(), dtoMapped.getPaidByMe());
            assertEquals(bizEventsPaidNoticeDTO.getIsDebtor(), dtoMapped.getRegisteredToMe());
    }
}