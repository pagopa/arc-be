package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  AccessTokenBuilderService accessTokenBuilderService;
  ObjectMapper objectMapper;

  CustomAuthenticationSuccessHandler(
      AccessTokenBuilderService accessTokenBuilderService,
      ObjectMapper objectMapper){
    this.accessTokenBuilderService = accessTokenBuilderService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    TokenResponse accessToken = accessTokenBuilderService.build();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write( objectMapper.writeValueAsString(accessToken) );
    response.getWriter().flush();
  }

}
