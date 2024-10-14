package it.gov.pagopa.arc.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    int status = response.getStatus();
    String message;

    switch (status) {
      case HttpServletResponse.SC_NOT_FOUND:
        message = "Missing parameters";
        break;
      case HttpServletResponse.SC_BAD_REQUEST:
        message = "Resource not found";
        break;
      case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
        message = "Internal server error";
        break;
      default:
        status = HttpServletResponse.SC_UNAUTHORIZED;
        message = "Unauthorized access, please login.";
        break;
    }

    // Set the response status and write the JSON response
    response.setStatus(status);
    response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    response.getWriter().flush(); // Ensure the writer is flushed

  }

}
