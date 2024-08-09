package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken.ZendeskAssistanceTokenResponseMapper;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ZendeskAssistanceTokenServiceImpl implements ZendeskAssistanceTokenService{
    private final ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilder;
    private final ZendeskAssistanceTokenConfig zendeskAssistanceToken;
    private final ZendeskAssistanceTokenResponseMapper zendeskAssistanceTokenResponseMapper;

    public ZendeskAssistanceTokenServiceImpl(ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilder, ZendeskAssistanceTokenConfig zendeskAssistanceToken, ZendeskAssistanceTokenResponseMapper zendeskAssistanceTokenResponseMapper) {
        this.zendeskAssistanceTokenBuilder = zendeskAssistanceTokenBuilder;
        this.zendeskAssistanceToken = zendeskAssistanceToken;
        this.zendeskAssistanceTokenResponseMapper = zendeskAssistanceTokenResponseMapper;
    }

    @Override
    public ZendeskAssistanceTokenResponse retrieveZendeskAssistanceTokenResponse(@NotNull String userEmail) {
        String assistanceToken = zendeskAssistanceTokenBuilder.buildZendeskAssistanceToken(userEmail);
        String returnTo = zendeskAssistanceToken.getReturnTo();

        return zendeskAssistanceTokenResponseMapper.toZendeskAssistanceTokenResponse(assistanceToken, returnTo);
    }
}
