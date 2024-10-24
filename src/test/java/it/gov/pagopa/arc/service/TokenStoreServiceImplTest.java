package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenStoreServiceImplTest {

  @Mock
  TokenStoreServiceImpl tokenStoreService;
  private final Map<String, Object> attributes = Map.of(
      "sub", "123456",
      "fiscalNumber", "789012",
      "familyName", "Polo",
      "name", "Marco",
      "email", "marco.polo@example.com",
      "iss", "issuer"
  );
  private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  @BeforeEach
  void setUp(){
    tokenStoreService = new TokenStoreServiceImpl();
  }
  @Test
  void givenAccessTokenAndUserInfoThenSaveAndRetrieveTheSameData() {
    IamUserInfoDTO userInfo = IamUserInfoDTO.map2IamUserInfoDTO(attributes);

    String accessToken = token;

    // When
    IamUserInfoDTO result = tokenStoreService.save(accessToken, userInfo);

    // Then
    Assertions.assertSame(userInfo, result);

  }

  @Test
  void givenAccessTokenAndUserInfoThenRemoveTokenAndNonInfoShouldBeFound(){
    // Given
    String accessToken = token;

    // When
    IamUserInfoDTO result = tokenStoreService.getUserInfo(accessToken);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void givenAccessThenRemoveToken(){
    // Given
    String accessToken = token;

    // When
    IamUserInfoDTO result = tokenStoreService.delete(accessToken);

    // Then
    Assertions.assertNull(result);
  }

}