package it.gov.pagopa.arc.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;

public interface GPDConnector {
    GPDPaymentNoticeDetailsDTO getPaymentNoticeDetails(String userId, String organizationFiscalCode, String iupd);
}
