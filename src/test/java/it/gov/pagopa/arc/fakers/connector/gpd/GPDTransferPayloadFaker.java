package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;

public class GPDTransferPayloadFaker {
    public static GPDTransferPayloadDTO mockInstance(String organizationFiscalCode) {
        return mockInstanceBuilder(organizationFiscalCode).build();
    }
    private static GPDTransferPayloadDTO.GPDTransferPayloadDTOBuilder mockInstanceBuilder(String organizationFiscalCode){
        return GPDTransferPayloadDTO.builder()
                .idTransfer("1")
                .organizationFiscalCode(organizationFiscalCode)
                .companyName("ORGANIZATION_NAME")
                .amount(966L)
                .remittanceInformation("Test Pull - unica opzione")
                .category("9/0101108TS/")
                .iban("IT39X0300203280451585346538");
    }
}
