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

class InMemoryOAuth2AuthorizationRequestRepositoryTest {

  private InMemoryOAuth2AuthorizationRequestRepository repository;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private OAuth2AuthorizationRequest authorizationRequest;
  private OAuth2StateStoreRepository oAuth2AuthorizationRequest;

  @BeforeEach
  void setUp() {
    oAuth2AuthorizationRequest = mock(OAuth2StateStoreRepository.class);
    repository = new InMemoryOAuth2AuthorizationRequestRepository(oAuth2AuthorizationRequest);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    authorizationRequest = mock(OAuth2AuthorizationRequest.class);
  }

  @Test
  void givenAuthorizationRequestThenSaveIt() {
    // Setup
    String state = "state123";
    when(authorizationRequest.getState()).thenReturn(state);
    when(request.getParameter("state")).thenReturn(state);
    when(oAuth2AuthorizationRequest.getOAuth2AuthorizationRequest(state)).thenReturn(authorizationRequest);

    // Execute
    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Verify
    assertEquals(authorizationRequest, repository.loadAuthorizationRequest(request));

    when(request.getParameter("state")).thenReturn(null);
  }

  @Test
  void givenAuthorizationRequestThenFailCauseStateNotInRequest() {
    when(authorizationRequest.getState()).thenReturn(null);
    when(request.getParameter("state")).thenReturn(null);
    when(oAuth2AuthorizationRequest.getOAuth2AuthorizationRequest("state")).thenReturn(null);
    // Execute
    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Verify
    assertNull(repository.loadAuthorizationRequest(request));
  }

  @Test
  void testLoadAuthorizationRequest() {
    // Setup
    String state = "state123";
    when(request.getParameter("state")).thenReturn(state);
    when(authorizationRequest.getState()).thenReturn(state);
    when(oAuth2AuthorizationRequest.getOAuth2AuthorizationRequest(state)).thenReturn(authorizationRequest);

    repository.saveAuthorizationRequest(authorizationRequest, request, response);

    // Execute
    OAuth2AuthorizationRequest loadedRequest = repository.loadAuthorizationRequest(request);

    // Verify
    assertNotNull(loadedRequest);
    assertEquals(authorizationRequest, loadedRequest);
  }

  @Test
  void givenValidStateThenRemoveAuthRequest() {
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
  void givenInvalidStateThenFailToRemoveAuthRequest() {
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