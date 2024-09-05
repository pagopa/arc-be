package it.gov.pagopa.arc.security;

import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

class CustomLogoutSuccessHandlerTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Authentication authentication;

  private CustomLogoutSuccessHandler logoutSuccessHandler;

  @Test
  void testOnLogoutSuccess() throws IOException {
    MockitoAnnotations.openMocks(this);
    logoutSuccessHandler = new CustomLogoutSuccessHandler();

    logoutSuccessHandler.onLogoutSuccess(request, response, authentication);

    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}