package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final AccessTokenBuilderService accessTokenBuilderService;
  private final ObjectMapper objectMapper;
  private final TokenStoreService tokenStoreService;
  private final JWTConfiguration jwtConfiguration;
  private final Set<String> cfWhitelist;

  public CustomAuthenticationSuccessHandler(
      AccessTokenBuilderService accessTokenBuilderService,
      ObjectMapper objectMapper,
      JWTConfiguration jwtConfiguration,
      TokenStoreService tokenStoreService,
      @Value("${white-list-users}") Set<String> cfWhitelist){
    this.accessTokenBuilderService = accessTokenBuilderService;
    this.objectMapper = objectMapper;
    this.jwtConfiguration = jwtConfiguration;
    this.tokenStoreService = tokenStoreService;
    this.cfWhitelist = cfWhitelist;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
    IamUserInfoDTO userInfoDTO = IamUserInfoDTO.map2IamUserInfoDTO( oauth2Token.getPrincipal().getAttributes());
    String body;
    if( isInWhiteList(userInfoDTO.getFiscalCode()) ){
      TokenResponse accessToken = new TokenResponse(
          accessTokenBuilderService.build(),
          jwtConfiguration.getTokenType(),
          jwtConfiguration.getAccessToken().getExpireIn(),
          null,
          null);

      tokenStoreService.save( accessToken.getAccessToken() , userInfoDTO );
      body = objectMapper.writeValueAsString(accessToken);
    } else {
      authentication.setAuthenticated(false);
      response.setStatus(403);
      body = "{\"error\": \"User not allowed to access this application\"}";
    }
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(body);
    response.getWriter().flush();
  }

  private boolean isInWhiteList(String fiscalCode){
    return cfWhitelist.contains(fiscalCode);
  }

}
