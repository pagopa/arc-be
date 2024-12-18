package it.gov.pagopa.arc.fakers.connector.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferStampDTO;

public class GPDTransferStampDTOFaker {

    public static GPDTransferStampDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static GPDTransferStampDTO.GPDTransferStampDTOBuilder mockInstanceBuilder(){
        return GPDTransferStampDTO.builder()
                .hashDocument("HASH_DOCUMENT")
                .stampType("01")
                .provincialResidence("RM");
    }
}
