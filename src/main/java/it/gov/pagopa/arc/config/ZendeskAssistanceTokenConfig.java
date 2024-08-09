package it.gov.pagopa.arc.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zendesk-assistance")
public class ZendeskAssistanceTokenConfig {

    private String tokenType;
    private String returnTo;
    private AssistanceToken assistanceToken;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssistanceToken{
        private String productId;
        private String organization;
        private String privateKey;
    }

}
