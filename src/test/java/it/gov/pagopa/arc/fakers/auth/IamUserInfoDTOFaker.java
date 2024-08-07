package it.gov.pagopa.arc.fakers.auth;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Map;

public class IamUserInfoDTOFaker {

  public static IamUserInfoDTO mockInstance(){
    Map<String, Object> attributes = Map.of(
        "sub", "123456",
        "fiscalNumber", "789012",
        "familyName", "Polo",
        "name", "Marco",
        "email", "marco.polo@example.com",
        "iss", "issuer"
    );
    return IamUserInfoDTO.map2IamUserInfoDTO(attributes);
  }

}
