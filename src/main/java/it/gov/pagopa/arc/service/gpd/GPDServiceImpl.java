package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.connector.gpd.GPDConnector;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.gpd.PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;
import org.springframework.stereotype.Service;

@Service
public class GPDServiceImpl implements GPDService{
    private final GPDConnector gpdConnector;
    private final GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
    private final PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper;
    private final GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper paymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper;

    public GPDServiceImpl(GPDConnector gpdConnector, GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper, PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper, GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper paymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper) {
        this.gpdConnector = gpdConnector;
        this.gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper = gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
        this.paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper = paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper;
        this.paymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper = paymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper;
    }

    @Override
    public PaymentNoticeDetailsDTO retrievePaymentNoticeDetailsFromGPD(String userId, String organizationFiscalCode, String iupd) {
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetails = gpdConnector.getPaymentNoticeDetails(userId, organizationFiscalCode, iupd);
        return gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetails);
    }

    @Override
    public PaymentNoticeDetailsDTO generatePaymentNoticeFromGPD(IamUserInfoDTO iamUserInfoDTO, PaymentNoticePayloadDTO paymentNoticePayloadDTO) {
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper.map(iamUserInfoDTO, paymentNoticePayloadDTO);
        GPDPaymentNoticePayloadDTO gpdPaymentNoticeResponse = gpdConnector.generatePaymentNotice(paymentNoticePayloadDTO.getPaTaxCode(), gpdPaymentNoticePayloadDTO);

        return paymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticeResponse);
    }
}
