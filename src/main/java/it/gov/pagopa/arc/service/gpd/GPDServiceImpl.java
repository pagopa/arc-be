package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.connector.gpd.GPDConnector;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import org.springframework.stereotype.Service;

@Service
public class GPDServiceImpl implements GPDService{
    private final GPDConnector gpdConnector;
    private final GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;

    public GPDServiceImpl(GPDConnector gpdConnector, GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper) {
        this.gpdConnector = gpdConnector;
        this.gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper = gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
    }

    @Override
    public PaymentNoticeDetailsDTO retrievePaymentNoticeDetailsFromGPD(String userId, String organizationFiscalCode, String iupd) {
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetails = gpdConnector.getPaymentNoticeDetails(userId, organizationFiscalCode, iupd);
        return gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetails);
    }
}
