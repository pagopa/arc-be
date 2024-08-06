package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthServiceImplTest {

  @Mock
  IamUserInfoDTO2UserInfo iamUserInfoDTO2UserInfo;

  @Mock
  AuthServiceImpl authService;

  @BeforeEach
  void setup(){
    authService = new AuthServiceImpl(iamUserInfoDTO2UserInfo);
  }
  @Test
  void givenValidAuthenticationSessionThenRetrieveUserInfo() {

    Authentication authentication = Mockito.mock(Authentication.class);
    // Mockito.whens() for your authorization object
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    securityContext.getAuthentication().getPrincipal();

    Map<String,String> user = Map.of(
        "userId", "_3948cf69b1dc05b50e1f13a4b14de22f",
        "fiscalCode", "TINIT-PLOMRC01P30L736Y",
        "familyName", "Polo",
        "name","Marco",
        "email", "ilmilione@virgilio.it"
    );

  }

  @Test
  void givenInvalidAuthenticationSessionThenGetException() {


  }

}