package it.gov.pagopa.arc.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

public class InMemoryOAuth2AuthorizationRequestRepository implements
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private final Map<String, OAuth2AuthorizationRequest> authorizationRequestMap = new ConcurrentHashMap<>();
  private static final String STATE = "state";

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    String state = request.getParameter(STATE);
    if (StringUtils.hasText(state)) {
      return authorizationRequestMap.get(state);
    }
    return null;
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
    String state = authorizationRequest.getState();
    if (StringUtils.hasText(state)) {
      // Save the authorization request in the map using the state as the key
      authorizationRequestMap.put(state, authorizationRequest);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
      HttpServletResponse response) {
    String state = request.getParameter(STATE);
    if (StringUtils.hasText(state)) {
      // Remove and return the authorization request from the map
      return authorizationRequestMap.remove(state);
    }
    return null;
  }

}
