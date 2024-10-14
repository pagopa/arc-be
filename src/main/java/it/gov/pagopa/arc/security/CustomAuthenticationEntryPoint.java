package it.gov.pagopa.arc.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    int status = 401;
    String message;

    if(response.getStatus()>=400){
      status = response.getStatus();
      //message = String.format("{\"error\": \"%s\"}", response.get);
    }

    response.setStatus(status);
    //response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    response.getWriter().flush(); // Ensure the writer is flushed

  }

}
