package it.gov.pagopa.arc.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    if( response.getStatus() >= 400 ){
      response.setStatus( response.getStatus() );
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
    }

  }

}
