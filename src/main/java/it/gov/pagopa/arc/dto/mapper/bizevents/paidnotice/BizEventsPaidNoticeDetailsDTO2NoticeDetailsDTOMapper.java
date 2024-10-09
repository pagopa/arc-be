package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsCartItem2CartItemDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper.class, BizEventsCartItem2CartItemDTOMapper.class})
public interface BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper {
    NoticeDetailsDTO toNoticeDetailsDTO(BizEventsPaidNoticeDetailsDTO bizEventsPaidNoticeDetailsDTO);
}
