package it.gov.pagopa.arc.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class IamUserInfoDTOTest {

  @Test
  public void testMap2IamUserInfoDTO() {
    Map<String, Object> attributes = Map.of(
        "sub", "123456",
        "fiscalNumber", "789012",
        "familyName", "Polo",
        "name", "Marco",
        "email", "marco.polo@example.com",
        "iss", "issuer"
    );

    IamUserInfoDTO userInfo = IamUserInfoDTO.map2IamUserInfoDTO(attributes);

    assertEquals("123456", userInfo.getUserId());
    assertEquals("789012", userInfo.getFiscalCode());
    assertEquals("Polo", userInfo.getFamilyName());
    assertEquals("Marco", userInfo.getName());
    assertEquals("marco.polo@example.com", userInfo.getEmail());
    assertEquals("issuer", userInfo.getIssuer());
  }

  @Test
  public void testMap2IamUserInfoDTOWithMissingAttributes() {
    Map<String, Object> attributes = Map.of(
        "sub", "123456"
    );

    IamUserInfoDTO userInfo = IamUserInfoDTO.map2IamUserInfoDTO(attributes);

    assertEquals("123456", userInfo.getUserId());
    assertNull(userInfo.getFiscalCode());
    assertNull(userInfo.getFamilyName());
    assertNull(userInfo.getName());
    assertNull(userInfo.getEmail());
    assertNull(userInfo.getIssuer());
  }


}