package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsCartItem2CartItemDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper.class, BizEventsCartItem2CartItemDTOMapper.class})
public interface BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper {

    @Mapping(source = "infoNotice", target = "infoNotice", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "carts", target = "carts", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    NoticeDetailsDTO toNoticeDetailsDTO(BizEventsPaidNoticeDetailsDTO bizEventsPaidNoticeDetailsDTO);
}
