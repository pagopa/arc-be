package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenStoreServiceImplTest {

  @Mock
  private TokenStoreServiceImpl tokenStoreService;
  private final String token = "token";

  @BeforeEach
  void setUp(){
    tokenStoreService = new TokenStoreServiceImpl();
  }
  @Test
  void givenAccessTokenWhenUserLoggedThenReturnUserInfo() {
    IamUserInfoDTO userInfo = new IamUserInfoDTO();
    // When
    IamUserInfoDTO result = tokenStoreService.save(token, userInfo);

    // Then
    Assertions.assertSame(userInfo, result);

  }

  @Test
  void givenNotAuthenticatedAccessTokenWhenGetUserThenNull(){
    // When
    IamUserInfoDTO result = tokenStoreService.getUserInfo(token);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void givenAccessWhenDeleteThenNull(){
    // When
    IamUserInfoDTO result = tokenStoreService.delete(token);

    // Then
    Assertions.assertNull(result);
  }

}