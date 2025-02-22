package it.gov.pagopa.arc.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private AuthenticationException authException;

  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Test
  void givenExceptionDuringRequestValidationThenReturn404() throws IOException {
    PrintWriter writer = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(writer);
    when(response.getStatus()).thenReturn(404);

    customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
    customAuthenticationEntryPoint.commence(request,response,authException);
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  void givenExceptionDuringRequestValidationThenReturn401() throws IOException {
    PrintWriter writer = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(writer);

    customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
    customAuthenticationEntryPoint.commence(request,response,authException);
    verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Test
  void givenExceptionDuringRequestValidationThenReturn400() throws IOException {
    PrintWriter writer = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(writer);
    when(response.getStatus()).thenReturn(400);

    customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
    customAuthenticationEntryPoint.commence(request,response,authException);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

}