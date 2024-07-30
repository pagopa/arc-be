package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.config.JWTConfigurationTest;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

public class CustomAuthenticationSuccessHandlerTest {

  @Mock
  private AccessTokenBuilderService accessTokenBuilderService;

  @Mock
  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Mock
  private Authentication authentication;

  @BeforeEach
  void setUp(){
    JWTConfiguration jwtConfiguration = JWTConfigurationTest.getCorrectConfiguration();
    accessTokenBuilderService = new AccessTokenBuilderService(jwtConfiguration);
    customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(accessTokenBuilderService,new ObjectMapper(),jwtConfiguration);
  }
  @Test
  void givenAuthenticationRequestThenInResponseGetCustomTokenResponse() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();
    customAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,authentication);
    assertEquals(response.getHeader("Content-Type"),"application/json");
    TokenResponse token = customAuthenticationSuccessHandler.objectMapper.readValue(response.getContentAsString(),TokenResponse.class);
    assertNotNull(token.getAccessToken());
    assertNotNull(token.getTokenType());
    assertNotNull(token.getExpiresIn());
  }

}