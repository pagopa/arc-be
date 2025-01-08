package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionMetadataDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferStampDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDTransferStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GPDTransferDTOFaker {
    public static GPDTransferDTO mockInstance(Integer bias) {
        return mockInstanceBuilder(bias).build();
    }

    public static GPDTransferDTO.GPDTransferDTOBuilder mockInstanceBuilder(Integer bias) {
        GPDTransferStampDTO gpdTransferStampDTO = GPDTransferStampDTOFaker.mockInstance();
        GPDPaymentOptionMetadataDTO gpdPaymentOptionMetadataDTO = GPDPaymentOptionMetadataDTOFaker.mockInstance(bias);

        return GPDTransferDTO.builder()
                .organizationFiscalCode("12345678901")
                .companyName("Test Company")
                .idTransfer("TRANSFER12345")
                .amount(1000L)
                .remittanceInformation("Pagamento trasferimento")
                .category("Pagamento")
                .iban("IT60X0542811101000000123456")
                .postalIban("123456")
                .stamp(gpdTransferStampDTO)
                .insertedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .status(GPDTransferStatus.T_UNREPORTED)
                .lastUpdatedDate(LocalDateTime.parse("2024-10-30T23:59:59"))
                .transferMetadata(List.of(gpdPaymentOptionMetadataDTO));
    }
}
