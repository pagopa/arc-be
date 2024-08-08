package it.gov.pagopa.arc.fakers.auth;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Map;

public class IamUserInfoDTOFaker {

  public static IamUserInfoDTO mockInstance(){
    Map<String, Object> attributes = Map.of(
        "sub", "user_id",
        "fiscalNumber", "FISCAL-CODE789456",
        "familyName", "familyName",
        "name", "name",
        "email", "sample@sample.com",
        "iss", "issuer"
    );
    return IamUserInfoDTO.map2IamUserInfoDTO(attributes);
  }

}
