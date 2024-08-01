package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.config.JWTSampleConfiguration;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationSuccessHandlerTest {

  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Mock
  private TokenStoreService tokenStoreService;

  @BeforeEach
  void setUp(){
    tokenStoreService = new TokenStoreServiceImpl();
    JWTConfiguration jwtConfiguration = JWTSampleConfiguration.getCorrectConfiguration();
    customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(new AccessTokenBuilderService(jwtConfiguration),new ObjectMapper(),jwtConfiguration,tokenStoreService);
  }
  @Test
  void givenAuthenticationRequestThenInResponseGetCustomTokenResponse() throws IOException {
    String sampleIdToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik1hcmNvIiwiaWF0IjoxNTE2MjM5MDIyLCJmaXNjYWxOdW1iZXIiOiI3ODkwMTIiLCJmYW1pbHlOYW1lIjoiUG9sbyIsImVtYWlsIjoibWFyY28ucG9sb0BleGFtcGxlLmNvbSIsImlzcyI6Imlzc3VlciJ9.AErwXpbHrT_0WvN86QuQ0nvnZndVxt8jnmiD1lfj1_A";
    Consumer<Map<String, Object>> attributesConsumer = attributes -> {
      attributes.putAll(Map.of(
          "sub", "123456",
          "fiscalNumber", "789012",
          "familyName", "Polo",
          "name", "Marco",
          "email", "marco.polo@example.com",
          "iss", "issuer"
      ));
    };
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(3600);

    OidcIdToken idToken = OidcIdToken.withTokenValue(sampleIdToken)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .claims(attributesConsumer)
        .build();
    OAuth2User principal = new DefaultOidcUser(null,idToken);
    OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(principal,null,"test-client");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();

    customAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,oAuth2AuthenticationToken);

    assertEquals(response.getHeader("Content-Type"),"application/json");

    TokenResponse token = new ObjectMapper().readValue(response.getContentAsString(),TokenResponse.class);
    assertNotNull(token.getAccessToken());
    assertNotNull(tokenStoreService.getUserInfo(token.getAccessToken()));
    assertNotNull(token.getTokenType());
    assertNotNull(token.getExpiresIn());
  }

}