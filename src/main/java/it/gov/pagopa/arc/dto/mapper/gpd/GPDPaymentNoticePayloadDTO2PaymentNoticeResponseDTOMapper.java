package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GPDPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper {

    public PaymentNoticeResponseDTO map(GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO){

        if (gpdPaymentNoticePayloadDTO == null){
            return null;
        }

        List<GPDPaymentOptionPayloadDTO> paymentOptions = gpdPaymentNoticePayloadDTO.getPaymentOption();
        if (paymentOptions == null || paymentOptions.isEmpty()) {
            return null;
        }

        GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO = paymentOptions.get(0);
        List<GPDTransferPayloadDTO> transfers = gpdPaymentOptionPayloadDTO.getTransfer();
        if (transfers == null || transfers.isEmpty()) {
            return null;
        }

        GPDTransferPayloadDTO gpdTransferPayloadDTO = transfers.get(0);

        return PaymentNoticeResponseDTO.builder()
                .nav(gpdPaymentOptionPayloadDTO.getNav())
                .paTaxCode(gpdTransferPayloadDTO.getOrganizationFiscalCode())
                .paFullName(gpdTransferPayloadDTO.getCompanyName())
                .amount(gpdPaymentOptionPayloadDTO.getAmount())
                .description(gpdPaymentOptionPayloadDTO.getDescription())
                .build();
    }

}
