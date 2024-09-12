package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.DateTimeTruncate;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PullPaymentNoticeStatus2PaymentNoticeStatus.class, PullPaymentOptionDTO2PaymentOptionDTO.class, DateTimeTruncate.class})
public interface PullPaymentNoticeDTO2PaymentNoticeDTO {

    @Mapping(source = "insertedDate", target = "insertedDate",qualifiedByName = "truncateToSeconds")
    @Mapping(source = "publishDate", target = "publishDate",qualifiedByName = "truncateToSeconds")
    @Mapping(source = "validityDate", target = "validityDate",qualifiedByName = "truncateToSeconds")
    PaymentNoticeDTO toPaymentNoticeDTO(PullPaymentNoticeDTO source);

}


