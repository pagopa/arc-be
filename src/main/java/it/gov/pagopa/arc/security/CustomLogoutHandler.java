package it.gov.pagopa.arc.security;

import it.gov.pagopa.arc.service.TokenStoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomLogoutHandler implements LogoutHandler {

  private final TokenStoreService tokenStoreService;
  public CustomLogoutHandler(TokenStoreService tokenStoreService) {
    this.tokenStoreService = tokenStoreService;
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(authorization)) {
      String token = authorization.replace("Bearer ", "");
      tokenStoreService.delete(token);
      SecurityContextHolder.clearContext();
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

  }
}
