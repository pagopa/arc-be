package it.gov.pagopa.arc.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.service.AccessTokenValidationService;
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

  private final AccessTokenValidationService accessTokenValidationService;


  public JwtAuthenticationFilter(TokenStoreService tokenStoreService,
      AccessTokenValidationService accessTokenValidationService) {
    this.tokenStoreService = tokenStoreService;
    this.accessTokenValidationService = accessTokenValidationService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (StringUtils.hasText(authorization)) {
        String token = authorization.replace("Bearer ", "");
        accessTokenValidationService.validate(token);
        IamUserInfoDTO userInfo = tokenStoreService.getUserInfo(token);
        if(userInfo!=null){
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, null);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          // https://docs.spring.io/spring-security/site/docs/5.2.11.RELEASE/reference/html/overall-architecture.html#:~:text=SecurityContextHolder%2C%20SecurityContext%20and%20Authentication%20Objects
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (TokenExpiredException e){
      log.info("Provided token is expired: "+ e.getMessage());
    } catch (SignatureVerificationException e) {
      log.info("Provided signature is invalid: "+ e.getMessage());
    } catch (Exception e) {
      log.error("Something gone wrong ", e);
    }
    filterChain.doFilter(request, response);
  }

}
