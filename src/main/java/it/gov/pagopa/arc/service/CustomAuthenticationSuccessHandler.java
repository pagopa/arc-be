package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

  private final AuthService authService;

  CustomAuthenticationSuccessHandler(
      AccessTokenBuilderService accessTokenBuilderService,
      ObjectMapper objectMapper,
      JWTConfiguration jwtConfiguration,
      TokenStoreService tokenStoreService,
      AuthService authService){
    this.accessTokenBuilderService = accessTokenBuilderService;
    this.objectMapper = objectMapper;
    this.jwtConfiguration = jwtConfiguration;
    this.tokenStoreService = tokenStoreService;
    this.authService = authService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
    IamUserInfoDTO userInfoDTO = IamUserInfoDTO.map2IamUserInfoDTO( oauth2Token.getPrincipal().getAttributes());

    if( isInWhiteList(userInfoDTO.getFiscalCode()) ){
      TokenResponse accessToken = new TokenResponse(
          accessTokenBuilderService.build(),
          jwtConfiguration.getTokenType(),
          jwtConfiguration.getAccessToken().getExpireIn(),
          null,
          null);

      tokenStoreService.save( accessToken.getAccessToken() , userInfoDTO );

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write( objectMapper.writeValueAsString(accessToken) );
      response.getWriter().flush();
    } else {
      authentication.setAuthenticated(false);
      response.setStatus(403);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\": \"User not allowed to access this application\"}");
      response.getWriter().flush();
    }

  }

  boolean isInWhiteList(String fiscalCode){
    List<String> whiteListUsers = authService.getWhiteListUsers();

    if (whiteListUsers == null) {
      return false; // or handle the null case as needed
    }

    return whiteListUsers.stream().anyMatch(str -> str.equals(fiscalCode));
  }

}
