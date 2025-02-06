package it.gov.pagopa.arc.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;

public interface GPDConnector {
    GPDPaymentNoticeDetailsDTO getPaymentNoticeDetails(String userId, String organizationFiscalCode, String iupd);
    GPDPaymentNoticePayloadDTO generatePaymentNotice(String organizationFiscalCode, GPDPaymentNoticePayloadDTO paymentPositionPayload);
}
