package it.gov.pagopa.arc.security;

import static org.mockito.Mockito.doNothing;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.service.AccessTokenValidationService;
import it.gov.pagopa.arc.service.TokenStoreService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
@Import(JwtAuthenticationFilter.class)
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
  @Mock
  private TokenStoreService tokenStoreService;
  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Mock
  AccessTokenValidationService accessTokenValidationService;
  private final String sampleJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  private final String sampleJwtNotInStorage = "eaJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  @BeforeEach
  public void setUp() {
    SecurityContextHolder.clearContext();
  }
  @AfterEach
  public void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenValidAuthenticationTokenThenVerifyThatSecurityContextIsCreated()
      throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    IamUserInfoDTO userInfoDTO = IamUserInfoDTOFaker.mockInstance();
    Mockito.when(tokenStoreService.getUserInfo(sampleJwt)).thenReturn(userInfoDTO);
    doNothing().when(accessTokenValidationService).validate(sampleJwt);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization","Bearer "+sampleJwt);
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Assertions.assertNotNull(principal);

    Assertions.assertEquals(userInfoDTO.getUserId(),((IamUserInfoDTO)principal).getUserId());
    Assertions.assertEquals(userInfoDTO.getName(),((IamUserInfoDTO)principal).getName());
    Assertions.assertEquals(userInfoDTO.getFamilyName(),((IamUserInfoDTO)principal).getFamilyName());
    Assertions.assertEquals(userInfoDTO.getEmail(),((IamUserInfoDTO)principal).getEmail());
    Assertions.assertEquals(userInfoDTO.getFiscalCode(),((IamUserInfoDTO)principal).getFiscalCode());
  }

  @Test
  void givenValidAuthenticationTokenButNotStoredInMemoryThenVerifyThatSecurityContextIsNotCreated()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization","Bearer "+sampleJwtNotInStorage);
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void givenInValidAuthenticationTokenThenVerifyThatSecurityContextIsNotCreated()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization","Bearer ");
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void givenEmptyHeaderThenVerifyThatSecurityContextIsNotCreated()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    jwtAuthenticationFilter.doFilterInternal(request,response,new MockFilterChain());
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

}