package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses= PullPaymentNoticeDTO2PaymentNoticeDTO.class)
public interface PaymentNoticesListDTOMapper {

    default PaymentNoticesListDTO toPaymentNoticesListDTO(List<PaymentNoticeDTO> paymentNotices) {
        PaymentNoticesListDTO dto = new PaymentNoticesListDTO();
        dto.setPaymentNotices(paymentNotices);
        return dto;
    }
}
