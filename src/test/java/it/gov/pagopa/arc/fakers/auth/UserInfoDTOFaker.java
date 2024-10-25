package it.gov.pagopa.arc.fakers.auth;

import it.gov.pagopa.arc.model.generated.UserInfo;

public class UserInfoDTOFaker {

  public static UserInfo mockInstance(){
    return UserInfo.builder()
        .userId("user_id")
        .email("sample@sample.com")
        .name("name")
        .fiscalCode("FISCAL-CODE789456")
        .familyName("familyName")
        .build();
  }

}
