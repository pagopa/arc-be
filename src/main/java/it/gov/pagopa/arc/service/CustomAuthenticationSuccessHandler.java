package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final AccessTokenBuilderService accessTokenBuilderService;
  private final ObjectMapper objectMapper;
  private final JWTConfiguration jwtConfiguration;

  CustomAuthenticationSuccessHandler(
      AccessTokenBuilderService accessTokenBuilderService,
      ObjectMapper objectMapper,
      JWTConfiguration jwtConfiguration){
    this.accessTokenBuilderService = accessTokenBuilderService;
    this.objectMapper = objectMapper;
    this.jwtConfiguration = jwtConfiguration;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    TokenResponse accessToken = new TokenResponse(accessTokenBuilderService.build(),jwtConfiguration.getTokenType(),jwtConfiguration.getAccessToken().getExpireIn(),null,null);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write( objectMapper.writeValueAsString(accessToken) );
    response.getWriter().flush();
  }

}
