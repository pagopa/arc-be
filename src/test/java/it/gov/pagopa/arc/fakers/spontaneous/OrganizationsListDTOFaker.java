package it.gov.pagopa.arc.fakers.spontaneous;

import it.gov.pagopa.arc.model.generated.OrganizationDTO;
import it.gov.pagopa.arc.model.generated.OrganizationsListDTO;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsListDTOFaker {
    public static OrganizationsListDTO mockInstance(int bias){
        return mockInstanceBuilder(bias).build();
    }

    public static OrganizationsListDTO.OrganizationsListDTOBuilder mockInstanceBuilder(int bias){
        List<OrganizationDTO> organizations = new ArrayList<>();

        for (int start = 0; start < bias; start++) {
            OrganizationDTO organizationDTO = OrganizationDTOFaker.mockInstance(start);
            organizations.add(organizationDTO);
        }

        return OrganizationsListDTO.builder()
                .organizations(organizations);
    }
}
