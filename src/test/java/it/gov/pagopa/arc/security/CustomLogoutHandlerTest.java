package it.gov.pagopa.arc.security;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.arc.service.TokenStoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CustomLogoutHandlerTest {

  @Mock
  private TokenStoreService tokenStoreService;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private CustomLogoutHandler customLogoutHandler;

  @Test
  void givenLogoutRequestThenPerformLogout() {
    String token = "test-token";
    String authorizationHeader = "Bearer " + token;

    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);
    when(tokenStoreService.delete(token)).thenReturn(null);

    customLogoutHandler.logout(request, response, authentication);

    verify(tokenStoreService, times(1)).delete(token);
    verify(request, times(1)).getHeader(HttpHeaders.AUTHORIZATION);

    Assertions.assertNotNull(request.getHeader(HttpHeaders.AUTHORIZATION));
    Assertions.assertEquals(request.getHeader(HttpHeaders.AUTHORIZATION),authorizationHeader);
  }

  @Test
  void givenEmptyAuthHeaderThenPerformLogout(){
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

    customLogoutHandler.logout(request, response, authentication);

    verify(tokenStoreService, never()).delete(anyString());
    verify(request).getHeader(HttpHeaders.AUTHORIZATION);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

}