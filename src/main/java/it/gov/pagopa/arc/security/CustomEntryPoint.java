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
    if(response.getStatus() == 404){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Resource not found\"}");
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Unauthorized access, please login.\"}");
    }

  }

}
