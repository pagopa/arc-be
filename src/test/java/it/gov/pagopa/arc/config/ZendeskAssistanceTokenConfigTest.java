package it.gov.pagopa.arc.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ZendeskAssistanceTokenConfig.class)
@TestPropertySource(properties = {
        "zendesk-assistance.tokenType=JWT",
        "zendesk-assistance.return-to=help-center-url.com",
        "zendesk-assistance.assistance-token.product-id=prod-arc",
        "zendesk-assistance.assistance-token.organization=_hc_arc",
        "zendesk-assistance.assistance-token.private-key=fakeSecretZendesk"

})
class ZendeskAssistanceTokenConfigTest {
    @Value("${zendesk-assistance.tokenType}")
    private String tokenType;
    @Value("${zendesk-assistance.return-to}")
    private String returnTo;
    @Value("${zendesk-assistance.assistance-token.product-id}")
    private String productId;
    @Value("${zendesk-assistance.assistance-token.organization}")
    private String organization;
    @Value("${zendesk-assistance.assistance-token.private-key}")
    private String privateKey;

    @Autowired
    private ZendeskAssistanceTokenConfig zendeskAssistanceTokenConfig;

    @Test
    void givenValuesWhenZendeskAssistanceTokenConfigReturnPopulatedFields(){
        String initializedTokenType = zendeskAssistanceTokenConfig.getTokenType();
        String initializedReturnTo = zendeskAssistanceTokenConfig.getReturnTo();
        String initializedProductId = zendeskAssistanceTokenConfig.getAssistanceToken().getProductId();
        String initializedOrganization = zendeskAssistanceTokenConfig.getAssistanceToken().getOrganization();
        String initializedPrivateKey = zendeskAssistanceTokenConfig.getAssistanceToken().getPrivateKey();

        Assertions.assertEquals(tokenType, initializedTokenType);
        Assertions.assertEquals(returnTo, initializedReturnTo);
        Assertions.assertEquals(productId, initializedProductId);
        Assertions.assertEquals(organization, initializedOrganization);
        Assertions.assertEquals(privateKey, initializedPrivateKey);
    }
}
