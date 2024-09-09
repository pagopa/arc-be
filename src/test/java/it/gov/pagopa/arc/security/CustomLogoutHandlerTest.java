package it.gov.pagopa.arc.security;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.arc.service.TokenStoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

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
    when(tokenStoreService.delete(token)).thenReturn(Optional.empty());

    customLogoutHandler.logout(request, response, authentication);

    verify(tokenStoreService, times(1)).delete(token);
    verify(request, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
  }

}