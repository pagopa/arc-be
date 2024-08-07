package it.gov.pagopa.arc.security;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.service.TokenStoreService;
import it.gov.pagopa.arc.service.TokenStoreServiceImpl;
import it.gov.pagopa.arc.utils.SecurityUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

class JwtAuthenticationFilterTest {

  @Mock
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Mock
  private TokenStoreService tokenStoreService;

  private final String SAMPLE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  @BeforeEach
  public void setUp() {
    SecurityContextHolder.clearContext();
    tokenStoreService = new TokenStoreServiceImpl();
    jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenStoreService);
    tokenStoreService.save(SAMPLE_JWT,IamUserInfoDTOFaker.mockInstance());
  }
  @AfterEach
  public void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenValidAuthenticationTokenThenVerifyThatSecurityContextIsCreated()
      throws ServletException, IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization","Bearer "+SAMPLE_JWT);
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());
    Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  @Test
  void givenInValidAuthenticationTokenThenVerifyThatSecurityContextIsCreated()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization","Bearer ");
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

}