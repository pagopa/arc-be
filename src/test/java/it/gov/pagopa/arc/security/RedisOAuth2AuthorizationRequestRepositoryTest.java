package it.gov.pagopa.arc.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

class RedisOAuth2AuthorizationRequestRepositoryTest {

  private RedisOAuth2AuthorizationRequestRepository repository;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private OAuth2AuthorizationRequest authorizationRequest;
  private OAuth2StateStoreRepository oAuth2AuthorizationRequest;

  @BeforeEach
  void setUp() {
    oAuth2AuthorizationRequest = mock(OAuth2StateStoreRepository.class);
    repository = new RedisOAuth2AuthorizationRequestRepository(oAuth2AuthorizationRequest);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    authorizationRequest = mock(OAuth2AuthorizationRequest.class);
  }

  @Test
  void givenAuthorizationRequestWhenTokenExchangeThenOK() {
    // Setup
    String state = "state123";
    when(authorizationRequest.getState()).thenReturn(state);
    when(request.getParameter("state")).thenReturn(state);
    when(oAuth2AuthorizationRequest.get(state)).thenReturn(authorizationRequest);

    // Execute
    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Verify
    assertEquals(authorizationRequest, repository.loadAuthorizationRequest(request));

    when(request.getParameter("state")).thenReturn(null);
  }

  @Test
  void givenTokenRequestWhenStateNotInRequestThenFailCause() {
    when(authorizationRequest.getState()).thenReturn(null);
    when(request.getParameter("state")).thenReturn(null);
    when(oAuth2AuthorizationRequest.get("state")).thenReturn(null);
    // Execute
    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Verify
    assertNull(repository.loadAuthorizationRequest(request));
  }

  @Test
  void givenValidStateWhenLoadAuthotizationRequesteThenGetAuthRequest() {
    // Setup
    String state = "state123";
    when(request.getParameter("state")).thenReturn(state);
    when(authorizationRequest.getState()).thenReturn(state);
    when(oAuth2AuthorizationRequest.get(state)).thenReturn(authorizationRequest);

    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Execute
    OAuth2AuthorizationRequest loadedRequest = repository.loadAuthorizationRequest(request);

    // Verify
    assertNotNull(loadedRequest);
    assertEquals(authorizationRequest, loadedRequest);
  }

  @Test
  void givenValidStateWhenTokenExchangeThenRemoveAuthRequest() {
    // Setup
    String state = "state123";
    when(request.getParameter("state")).thenReturn(state);
    when(authorizationRequest.getState()).thenReturn(state);
    when(repository.removeAuthorizationRequest(request, response))
        .thenReturn(authorizationRequest)
        .thenReturn(null);

    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Execute
    OAuth2AuthorizationRequest removedRequest = repository.removeAuthorizationRequest(request,
        response);

    // Verify
    assertNotNull(removedRequest);
    assertEquals(authorizationRequest, removedRequest);
    assertNull(repository.loadAuthorizationRequest(request));
  }

  @Test
  void givenInvalidStateWhenTokenExchangeThenNull() {
    when(request.getParameter("state")).thenReturn(null);
    when(authorizationRequest.getState()).thenReturn(null);

    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Execute
    OAuth2AuthorizationRequest removedRequest = repository.removeAuthorizationRequest(request,
        response);

    // Verify
    assertNull(removedRequest);
  }

}