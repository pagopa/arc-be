package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken.ZendeskAssistanceTokenResponseMapper;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        log.info("[GET_ZENDESK_ASSISTANCE_TOKEN_RESPONSE] the creation of the jwt token was requested to request assistance on zendesk");
        String zendeskAssistanceToken = zendeskAssistanceTokenBuilder.buildZendeskAssistanceToken(userEmail);
        String returnTo = this.zendeskAssistanceTokenConfig.getReturnTo();
        String actionUrl = this.zendeskAssistanceTokenConfig.getActionUrl();

        return zendeskAssistanceTokenResponseMapper.toZendeskAssistanceTokenResponse(zendeskAssistanceToken, returnTo , actionUrl);
    }
}
