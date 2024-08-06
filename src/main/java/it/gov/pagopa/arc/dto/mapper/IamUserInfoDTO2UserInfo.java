package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class IamUserInfoDTO2UserInfo {

  public UserInfo mapIamUserToUserInfo(IamUserInfoDTO userInfoDTO){
    return UserInfo.builder()
        .name(userInfoDTO.getName())
        .userId(userInfoDTO.getUserId())
        .email(userInfoDTO.getEmail())
        .familyName(userInfoDTO.getFamilyName())
        .fiscalCode(userInfoDTO.getFiscalCode())
        .build();
  }

}
