package it.gov.pagopa.arc.security;

import it.gov.pagopa.arc.config.RedisConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Log4j2
@Service
public class InMemoryOAuth2AuthorizationRequestRepository implements
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private OAuth2StateStoreRepository oAuth2AuthorizationRequest;
  private static final String STATE = "state";

  public InMemoryOAuth2AuthorizationRequestRepository(OAuth2StateStoreRepository oAuth2AuthorizationRequest){
    this.oAuth2AuthorizationRequest = oAuth2AuthorizationRequest;

  }
  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    String state = request.getParameter(STATE);
    if (StringUtils.hasText(state)) {
      return oAuth2AuthorizationRequest.getOAuth2AuthorizationRequest(state);
    }
    return null;
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
    String state = authorizationRequest.getState();
    if (StringUtils.hasText(state)) {
      // Save the authorization request in the map using the state as the key
      log.info("Saving authorization request for state: {}", state);
      oAuth2AuthorizationRequest.save(state, authorizationRequest);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
      HttpServletResponse response) {
    String state = request.getParameter(STATE);
    if (StringUtils.hasText(state)) {
      // Remove and return the authorization request from the map
      return oAuth2AuthorizationRequest.delete(state);
    }
    return null;
  }



}
