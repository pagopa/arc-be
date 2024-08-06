package it.gov.pagopa.arc.security;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.service.TokenStoreService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenStoreService tokenStoreService;

  public JwtAuthenticationFilter(TokenStoreService tokenStoreService) {
    this.tokenStoreService = tokenStoreService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (StringUtils.hasText(authorization)) {
        Optional<IamUserInfoDTO> userInfo = tokenStoreService.getUserInfo(authorization.replace("Bearer ", ""));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo.get(), null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e){
      log.error("Something gone wrong while retrieving UserInfo", e);
    }

    filterChain.doFilter(request, response);
  }

}
