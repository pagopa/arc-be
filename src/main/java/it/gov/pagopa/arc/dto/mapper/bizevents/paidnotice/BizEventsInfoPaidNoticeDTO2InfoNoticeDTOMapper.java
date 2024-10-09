package it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsInfoPaidNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsUserDetail2UserDetailDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsWalletInfo2WalletInfoDTOMapper;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.InfoNoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {MapperUtilities.class, BizEventsWalletInfo2WalletInfoDTOMapper.class, BizEventsUserDetail2UserDetailDTOMapper.class})
public interface BizEventsInfoPaidNoticeDTO2InfoNoticeDTOMapper {

    @Mapping(source = "amount", target = "amount", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "euroToCents")
    @Mapping(source = "fee", target = "fee", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "euroToCents")
    @Mapping(source = "noticeDate", target = "noticeDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "dateStringToZonedDateTime")
    InfoNoticeDTO toInfoNoticeDTO(BizEventsInfoPaidNoticeDTO bizEventsInfoPaidNoticeDTO);
}
