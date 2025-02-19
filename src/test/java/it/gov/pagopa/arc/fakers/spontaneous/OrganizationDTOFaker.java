package it.gov.pagopa.arc.fakers.spontaneous;

import it.gov.pagopa.arc.model.generated.OrganizationDTO;

public class OrganizationDTOFaker {
    public static OrganizationDTO mockInstance(int bias){
        return mockInstanceBuilder(bias).build();
    }

    public static OrganizationDTO.OrganizationDTOBuilder mockInstanceBuilder(int bias){
       return OrganizationDTO.builder()
               .id(String.valueOf(bias))
               .paTaxCode("ORGANIZATION_FISCAL_CODE_" + bias)
               .paFullName("ORGANIZATION_FULL_NAME_" + bias)
               .ipaCode("ORGANIZATION_IPA_CODE_" + bias);

    }
}
