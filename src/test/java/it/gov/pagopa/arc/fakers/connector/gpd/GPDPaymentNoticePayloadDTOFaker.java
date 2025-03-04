package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;

import java.util.List;

public class GPDPaymentNoticePayloadDTOFaker {
    public static GPDPaymentNoticePayloadDTO mockInstance(String organizationFiscalCode) {
        return mockInstanceBuilder(organizationFiscalCode).build();
    }

    public static GPDPaymentNoticePayloadDTO.GPDPaymentNoticePayloadDTOBuilder mockInstanceBuilder(String organizationFiscalCode) {
        GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO = GPDPaymentOptionPayloadFaker.mockInstance(organizationFiscalCode);

        return GPDPaymentNoticePayloadDTO.builder()
                .iupd(organizationFiscalCode.concat("-1234567890"))
                .type(GPDDebtorType.F)
                .payStandIn(true)
                .companyName("ORGANIZATION_NAME")
                .fullName("USER_FULL_NAME")
                .fiscalCode("USER_DUMMY_FISCAL_CODE")
                .switchToExpired(true)
                .paymentOption(List.of(gpdPaymentOptionPayloadDTO));
    }
}
