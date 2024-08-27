package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

class SecurityUtilsTest {

  @AfterEach
  public void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenConfiguredSecurityContextThenRetrieveTheAuthenticatedUser(){
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            IamUserInfoDTOFaker.mockInstance(), null, null);
    authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    IamUserInfoDTO user = SecurityUtils.getPrincipal();
    Assertions.assertNotNull(user);
  }

  @Test
  void givenWrongConfiguredSecurityContextThenThrowException(){
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            new Object(), null, null);
    authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    RuntimeException ex = Assertions.assertThrows(RuntimeException.class, SecurityUtils::getPrincipal);
    Assertions.assertEquals("Invalid principal type: expected IamUserInfoDTO but got java.lang.Object", ex.getMessage());
  }

}