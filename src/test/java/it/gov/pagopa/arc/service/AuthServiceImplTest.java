package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

class AuthServiceImplTest {

  @InjectMocks
  IamUserInfoDTO2UserInfo mapper = Mappers.getMapper(IamUserInfoDTO2UserInfo.class);

  @Mock
  AuthServiceImpl authService;

  @BeforeEach
  void setup(){
    authService = new AuthServiceImpl(mapper);
    //given
    SecurityContextHolder.clearContext();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        IamUserInfoDTOFaker.mockInstance(), null, null);
    authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

  }

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void givenValidAuthenticationSessionThenRetrieveUserInfo() {
    //then
    Assertions.assertNotNull(authService.getUserLoginInfo());
  }

}