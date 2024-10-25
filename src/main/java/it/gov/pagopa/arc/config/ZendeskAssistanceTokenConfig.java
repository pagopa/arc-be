package it.gov.pagopa.arc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "zendesk-assistance")
public class ZendeskAssistanceTokenConfig {

    private String tokenType;
    private String returnTo;
    private String actionUrl;
    private AssistanceToken assistanceToken;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssistanceToken{
        private String productId;
        private String organization;
        private String privateKey;
    }

}
