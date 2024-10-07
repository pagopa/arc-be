package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.config.JWTSampleConfiguration;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import wiremock.org.apache.hc.core5.http.ContentType;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Mock
  private TokenStoreService tokenStoreService;

  @Mock
  private AuthService authService;

  @BeforeEach
  void setUp(){
    tokenStoreService = new TokenStoreServiceImpl();
    JWTConfiguration jwtConfiguration = JWTSampleConfiguration.getCorrectConfiguration();
    customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(
        new AccessTokenBuilderService(jwtConfiguration),
        new ObjectMapper(),
        jwtConfiguration,
        tokenStoreService,
        authService);
  }
  @Test
  void givenAuthenticationRequestThenInResponseGetCustomTokenResponse() throws IOException {
    Mockito.when(authService.getWhiteListUsers()).thenReturn(List.of("PLOMRC01P30L736Y"));
    OAuth2AuthenticationToken oAuth2AuthenticationToken = getAuthenticationToken();

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();

    customAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,oAuth2AuthenticationToken);

    assertEquals(ContentType.APPLICATION_JSON.getMimeType(),response.getContentType());

    TokenResponse token = new ObjectMapper().readValue(response.getContentAsString(),TokenResponse.class);
    assertNotNull(token.getAccessToken());
    assertNotNull(tokenStoreService.getUserInfo(token.getAccessToken()));
    assertNotNull(token.getTokenType());
    assertNotNull(token.getExpiresIn());
  }

  @Test
  void givenAuthenticationRequestThenInResponseGetForbidden()
      throws IOException {
    Mockito.when(authService.getWhiteListUsers()).thenReturn(null);
    OAuth2AuthenticationToken oAuth2AuthenticationToken = getAuthenticationToken();

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();

    customAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,oAuth2AuthenticationToken);

    assertEquals(ContentType.APPLICATION_JSON.getMimeType(), response.getContentType());
    assertEquals(403,response.getStatus());
  }

  private OAuth2AuthenticationToken getAuthenticationToken(){
    Consumer<Map<String, Object>> attributesConsumer = attributes -> {
      attributes.putAll(Map.of(
          "sub", "123456",
          "fiscalNumber", "TINIT-PLOMRC01P30L736Y",
          "familyName", "Polo",
          "name", "Marco",
          "email", "marco.polo@example.com",
          "iss", "issuer"
      ));
    };
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(3600);

    OidcIdToken idToken = OidcIdToken.withTokenValue(JWT.create().toString())
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .claims(attributesConsumer)
        .build();
    OAuth2User principal = new DefaultOidcUser(null,idToken);
    return new OAuth2AuthenticationToken(principal,null,"test-client");
  }

}