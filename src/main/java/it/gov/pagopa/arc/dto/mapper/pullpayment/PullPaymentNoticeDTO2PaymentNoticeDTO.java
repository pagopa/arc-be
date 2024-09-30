package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {PullPaymentNoticeStatus2PaymentNoticeStatus.class, PullPaymentOptionDTO2PaymentOptionDTO.class, MapperUtilities.class})
public interface PullPaymentNoticeDTO2PaymentNoticeDTO {

    @Mapping(source = "insertedDate", target = "insertedDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "truncateToSeconds")
    @Mapping(source = "publishDate", target = "publishDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "truncateToSeconds")
    @Mapping(source = "validityDate", target = "validityDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "truncateToSeconds")
    PaymentNoticeDTO toPaymentNoticeDTO(PullPaymentNoticeDTO source);

}


