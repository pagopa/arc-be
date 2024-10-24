package it.gov.pagopa.arc.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InMemoryOAuth2AuthorizationRequestRepository implements
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private final OAuth2StateStoreRepository oAuth2AuthorizationRequest;
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
      oAuth2AuthorizationRequest.save(state, authorizationRequest);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
      HttpServletResponse response) {
    String state = request.getParameter(STATE);
    if (StringUtils.hasText(state)) {
      // Remove and return the authorization request from the map
      OAuth2AuthorizationRequest result = loadAuthorizationRequest(request);
      if (result!=null){
        oAuth2AuthorizationRequest.delete(state);
        return result;
      }
    }
    return null;
  }

}
