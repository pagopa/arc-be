package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class TokenStoreServiceImplTest {

  @Mock
  TokenStoreServiceImpl tokenStoreService;

  private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  private final Map<String, Object> attributes = Map.of(
      "sub", "123456",
      "fiscalNumber", "789012",
      "familyName", "Polo",
      "name", "Marco",
      "email", "marco.polo@example.com",
      "iss", "issuer"
  );
  @BeforeEach
  void setUp(){
    tokenStoreService = new TokenStoreServiceImpl();
  }
  @Test
  void givenAccessTokenAndUserInfoThenSaveAndRetrieveTheSameData() {
    String wrongToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    IamUserInfoDTO userInfo = IamUserInfoDTO.map2IamUserInfoDTO(attributes);
    tokenStoreService.save(token,userInfo);

    assertNotNull(tokenStoreService.getUserInfo(token));

    assertEquals(attributes.get("sub"),tokenStoreService.getUserInfo(token).getUserId());
    assertEquals(attributes.get("fiscalNumber"),tokenStoreService.getUserInfo(token).getFiscalCode());
    assertEquals(attributes.get("familyName"),tokenStoreService.getUserInfo(token).getFamilyName());
    assertEquals(attributes.get("name"),tokenStoreService.getUserInfo(token).getName());
    assertEquals(attributes.get("email"),tokenStoreService.getUserInfo(token).getEmail());
    assertEquals(attributes.get("iss"),tokenStoreService.getUserInfo(token).getIssuer());

    assertNull(tokenStoreService.getUserInfo(wrongToken));
  }

  @Test
  void givenAccessTokenAndUserInfoThenRemoveTokenAndNonInfoShouldBeFound(){
    IamUserInfoDTO userInfo = IamUserInfoDTO.map2IamUserInfoDTO(attributes);
    tokenStoreService.save(token,userInfo);

    tokenStoreService.delete(token);

    assertNull(tokenStoreService.getUserInfo(token));
  }

}