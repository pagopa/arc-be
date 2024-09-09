package it.gov.pagopa.arc.security;

import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class CustomLogoutSuccessHandlerTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Authentication authentication;

  private CustomLogoutSuccessHandler logoutSuccessHandler;

  @Test
  void testOnLogoutSuccess() {
    logoutSuccessHandler = new CustomLogoutSuccessHandler();

    logoutSuccessHandler.onLogoutSuccess(request, response, authentication);

    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}