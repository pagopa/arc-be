package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.OrganizationsListDTO;

public interface PaymentNoticeSpontaneousService {
    OrganizationsListDTO retrieveOrganizations(String userId);
}
