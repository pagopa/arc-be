package it.gov.pagopa.arc.service;

import static org.mockito.ArgumentMatchers.any;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.auth.UserInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
  @Mock
  IamUserInfoDTO2UserInfo mapperMock;
  @Autowired
  private AuthService authService;

  private final IamUserInfoDTO iamUserInfoDTO = IamUserInfoDTOFaker.mockInstance();

  @BeforeEach
  void setUp() {
    authService = new AuthServiceImpl(mapperMock);
    SecurityContextHolder.clearContext();
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        iamUserInfoDTO, null, null);
    authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Test
  void givenValidAuthenticationSessionThenRetrieveUserInfo() {
    //given
    UserInfo userInfo = UserInfoDTOFaker.mockInstance();
    Mockito.when(mapperMock.mapIamUserToUserInfo(iamUserInfoDTO)).thenReturn(userInfo);
    //when
    UserInfo user = authService.getUserLoginInfo();
    //then
    Assertions.assertNotNull(user);

    Assertions.assertEquals(user.getUserId(),iamUserInfoDTO.getUserId());
    Assertions.assertEquals(user.getName(),iamUserInfoDTO.getName());
    Assertions.assertEquals(user.getFamilyName(),iamUserInfoDTO.getFamilyName());
    Assertions.assertEquals(user.getEmail(),iamUserInfoDTO.getEmail());
    Assertions.assertEquals(user.getFiscalCode(),iamUserInfoDTO.getFiscalCode());

    Mockito.verify(mapperMock).mapIamUserToUserInfo(any());
  }

}