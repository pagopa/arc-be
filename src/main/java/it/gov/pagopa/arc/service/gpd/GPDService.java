package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;

public interface GPDService {
    PaymentNoticeDetailsDTO retrievePaymentNoticeDetailsFromGPD(String userId, String organizationFiscalCode, String iupd);
    PaymentNoticeDetailsDTO generatePaymentNoticeFromGPD(IamUserInfoDTO iamUserInfoDTO, PaymentNoticePayloadDTO paymentNoticePayloadDTO);
}
