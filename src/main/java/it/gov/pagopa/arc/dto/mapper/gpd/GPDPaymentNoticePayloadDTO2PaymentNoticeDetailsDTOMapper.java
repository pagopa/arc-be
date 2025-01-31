package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentNoticeStatus;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper {

    public PaymentNoticeDetailsDTO map(GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO){

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

        return PaymentNoticeDetailsDTO.builder()
                .iupd(gpdPaymentNoticePayloadDTO.getIupd())
                .paTaxCode(gpdTransferPayloadDTO.getOrganizationFiscalCode())
                .paFullName(gpdTransferPayloadDTO.getCompanyName())
                .status(gPDPaymentNoticeStatusToPaymentNoticeDetailsStatus(gpdPaymentNoticePayloadDTO.getStatus()))
                .paymentOptions(mapToPaymentOptionDetailsDTO(gpdPaymentOptionPayloadDTO))
                .build();
    }

    private List<PaymentOptionDetailsDTO> mapToPaymentOptionDetailsDTO(GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO){

        return Collections.singletonList(
                PaymentOptionDetailsDTO.builder()
                        .nav(gpdPaymentOptionPayloadDTO.getNav())
                        .iuv(gpdPaymentOptionPayloadDTO.getIuv())
                        .amount(gpdPaymentOptionPayloadDTO.getAmount())
                        .description(gpdPaymentOptionPayloadDTO.getDescription())
                        .isPartialPayment(false)
                        .dueDate(MapperUtilities.convertToZonedDateTimeAndTruncateSeconds(gpdPaymentOptionPayloadDTO.getDueDate()))
                        .notificationFee(gpdPaymentOptionPayloadDTO.getNotificationFee())
                        .build());
    }

    private PaymentNoticeDetailsStatus gPDPaymentNoticeStatusToPaymentNoticeDetailsStatus(GPDPaymentNoticeStatus gPDPaymentNoticeStatus) {
        if ( gPDPaymentNoticeStatus == null ) {
            return null;
        }

        return switch (gPDPaymentNoticeStatus) {
            case DRAFT -> PaymentNoticeDetailsStatus.DRAFT;
            case PUBLISHED -> PaymentNoticeDetailsStatus.PUBLISHED;
            case VALID -> PaymentNoticeDetailsStatus.VALID;
            case INVALID -> PaymentNoticeDetailsStatus.INVALID;
            case EXPIRED -> PaymentNoticeDetailsStatus.EXPIRED;
            case PARTIALLY_PAID -> PaymentNoticeDetailsStatus.PARTIALLY_PAID;
            case PAID -> PaymentNoticeDetailsStatus.PAID;
            case REPORTED -> PaymentNoticeDetailsStatus.REPORTED;
        };
    }


}
