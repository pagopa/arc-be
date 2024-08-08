package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import it.gov.pagopa.arc.model.generated.UserInfo;
import it.gov.pagopa.arc.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

  private final IamUserInfoDTO2UserInfo iamUserInfoDTO2UserInfo;

  public AuthServiceImpl(IamUserInfoDTO2UserInfo iamUserInfoDTO2UserInfo){
    this.iamUserInfoDTO2UserInfo = iamUserInfoDTO2UserInfo;
  }

  @Override
  public UserInfo getUserLoginInfo() {
    IamUserInfoDTO sessionToken = SecurityUtils.getPrincipal();
    return iamUserInfoDTO2UserInfo.mapIamUserToUserInfo(sessionToken);
  }

}
