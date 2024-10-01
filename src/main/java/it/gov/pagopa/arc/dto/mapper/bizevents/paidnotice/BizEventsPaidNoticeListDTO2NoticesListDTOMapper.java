package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BizEventsPaidNoticeListDTO2NoticesListDTOMapper {

    default NoticesListDTO toNoticesListDTO(List<NoticeDTO> noticeDTOList){
        return  NoticesListDTO.builder().notices(noticeDTOList).build();
    }
}
