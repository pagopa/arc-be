package it.gov.pagopa.arc.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class IamUserInfoDTO {


  private String userId;
  private String fiscalCode;
  private String familyName;
  private String name;
  private String email;
  private String issuer;

  public static IamUserInfoDTO map2IamUserInfoDTO(Map<String, Object> attributes) {
    return new IamUserInfoDTO(
        getStringFromMap(attributes, "sub"),
        getStringFromMap(attributes, "fiscalNumber"),
        getStringFromMap(attributes, "familyName"),
        getStringFromMap(attributes, "name"),
        getStringFromMap(attributes, "email"),
        getStringFromMap(attributes, "iss")
    );
  }

  private static String getStringFromMap(Map<String, Object> map, String key) {
    return map.containsKey(key) ? map.get(key).toString() : null;
  }

}