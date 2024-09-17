package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.auth.UserInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;
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

  @Mock
  private TokenStoreService tokenStoreService;
  @Mock
  private AccessTokenBuilderService accessTokenBuilderService;

  @BeforeEach
  void setUp() {
    authService = new AuthServiceImpl(mapperMock,tokenStoreService,accessTokenBuilderService);
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
    assertNotNull(user);

    assertEquals(user.getUserId(),iamUserInfoDTO.getUserId());
    assertEquals(user.getName(),iamUserInfoDTO.getName());
    assertEquals(user.getFamilyName(),iamUserInfoDTO.getFamilyName());
    assertEquals(user.getEmail(),iamUserInfoDTO.getEmail());
    assertEquals(user.getFiscalCode(),iamUserInfoDTO.getFiscalCode());

    Mockito.verify(mapperMock).mapIamUserToUserInfo(any());
  }

  @Test
  void verifyGetTestUserLoginInfo(){
    Mockito.when(accessTokenBuilderService.build()).thenReturn("token");

    TokenResponse tokenResponse = authService.getTestUserLoginInfo();
    assertNotNull(tokenResponse);
    assertEquals("Bearer", tokenResponse.getTokenType());
    assertNotNull(tokenResponse.getAccessToken());
  }

}