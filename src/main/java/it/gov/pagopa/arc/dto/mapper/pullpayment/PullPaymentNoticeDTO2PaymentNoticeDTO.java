package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PullPaymentNoticeStatus2PaymentNoticeStatus.class, PullPaymentOptionDTO2PaymentOptionDTO.class})
public interface PullPaymentNoticeDTO2PaymentNoticeDTO {

    PaymentNoticeDTO toPaymentNoticeDTO(PullPaymentNoticeDTO source);

}


