package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDTO2NoticeDTOMapper;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeListDTO2NoticesListDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BizEventsPaidNoticeDTO2NoticeDTOMapper.class, BizEventsPaidNoticeListDTO2NoticesListDTOMapper.class})
public interface NoticesListResponseDTOMapper {

    default NoticesListResponseDTO mapToFullResponse(BizEventsPaidNoticeDTO2NoticeDTOMapper bizEventsPaidNoticeDTO2NoticeDTOMapper,  BizEventsPaidNoticeListDTO2NoticesListDTOMapper bizEventsPaidNoticeListDTO2NoticesListDTOMapper, List<BizEventsPaidNoticeDTO> bizEventsPaidNoticeDTOList, String continuationToken) {

        List<NoticeDTO> listOfNoticeDTO = bizEventsPaidNoticeDTO2NoticeDTOMapper.toNoticeDTOList(bizEventsPaidNoticeDTOList);

        NoticesListDTO noticesListDTO = bizEventsPaidNoticeListDTO2NoticesListDTOMapper.toNoticesListDTO(listOfNoticeDTO);

        return NoticesListResponseDTO.builder()
                .noticesListDTO(noticesListDTO)
                .continuationToken(continuationToken)
                .build();

    }

}
