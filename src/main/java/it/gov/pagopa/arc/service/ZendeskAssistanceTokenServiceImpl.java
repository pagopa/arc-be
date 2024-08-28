package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken.ZendeskAssistanceTokenResponseMapper;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ZendeskAssistanceTokenServiceImpl implements ZendeskAssistanceTokenService{
    private final ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilder;
    private final ZendeskAssistanceTokenConfig zendeskAssistanceTokenConfig;
    private final ZendeskAssistanceTokenResponseMapper zendeskAssistanceTokenResponseMapper;

    public ZendeskAssistanceTokenServiceImpl(ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilder, ZendeskAssistanceTokenConfig zendeskAssistanceTokenConfig, ZendeskAssistanceTokenResponseMapper zendeskAssistanceTokenResponseMapper) {
        this.zendeskAssistanceTokenBuilder = zendeskAssistanceTokenBuilder;
        this.zendeskAssistanceTokenConfig = zendeskAssistanceTokenConfig;
        this.zendeskAssistanceTokenResponseMapper = zendeskAssistanceTokenResponseMapper;
    }

    @Override
    public ZendeskAssistanceTokenResponse retrieveZendeskAssistanceTokenResponse(@NotNull String userEmail) {
        String zendeskAssistanceToken = zendeskAssistanceTokenBuilder.buildZendeskAssistanceToken(userEmail);
        String returnTo = this.zendeskAssistanceTokenConfig.getReturnTo();

        return zendeskAssistanceTokenResponseMapper.toZendeskAssistanceTokenResponse(zendeskAssistanceToken, returnTo);
    }
}
