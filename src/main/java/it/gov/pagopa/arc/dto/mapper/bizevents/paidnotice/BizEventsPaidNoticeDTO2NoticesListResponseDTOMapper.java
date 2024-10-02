package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = MapperUtilities.class)
public interface BizEventsPaidNoticeDTO2NoticesListResponseDTOMapper {

    @Mapping(source = "amount", target = "amount", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "euroToCents")
    @Mapping(source = "noticeDate", target = "noticeDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "dateStringToZonedDateTime")
    @Mapping(source = "isPayer", target = "paidByMe")
    @Mapping(source = "isDebtor", target = "registeredToMe")
    NoticeDTO toNoticeDTO(BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO);

    default NoticesListDTO toNoticeListDTO(BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO){
        List<NoticeDTO> listOfNoticeDTO = bizEventsPaidNoticeListDTO.getNotices().stream().map(this::toNoticeDTO).toList();
        return NoticesListDTO.builder().notices(listOfNoticeDTO).build();
    }

}
