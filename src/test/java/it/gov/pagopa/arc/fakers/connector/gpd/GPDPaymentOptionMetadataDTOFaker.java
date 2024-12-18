package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionMetadataDTO;

public class GPDPaymentOptionMetadataDTOFaker {
    public static GPDPaymentOptionMetadataDTO mockInstance(Integer bias) {
        return mockInstanceBuilder(bias).build();
    }

    public static GPDPaymentOptionMetadataDTO.GPDPaymentOptionMetadataDTOBuilder mockInstanceBuilder(Integer bias){
        return GPDPaymentOptionMetadataDTO.builder()
                .key("KEY%d".formatted(bias))
                .value("VALUE%d".formatted(bias));
    }
}
