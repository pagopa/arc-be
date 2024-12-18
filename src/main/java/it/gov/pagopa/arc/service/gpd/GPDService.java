package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;

public interface GPDService {
    PaymentNoticeDetailsDTO retrievePaymentNoticeDetailsFromGPD(String userId, String organizationFiscalCode, String iupd);
}
