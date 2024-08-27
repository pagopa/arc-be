package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import jakarta.validation.constraints.NotNull;

public interface ZendeskAssistanceTokenService {
    ZendeskAssistanceTokenResponse retrieveZendeskAssistanceTokenResponse(@NotNull String userEmail);
}
