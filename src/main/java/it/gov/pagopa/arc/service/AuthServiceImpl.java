package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.IamUserInfoDTO2UserInfo;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;
import it.gov.pagopa.arc.utils.SecurityUtils;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

  private final IamUserInfoDTO2UserInfo iamUserInfoDTO2UserInfo;

  private final TokenStoreService tokenStoreService;

  private final AccessTokenBuilderService accessTokenBuilderService;

  public AuthServiceImpl(IamUserInfoDTO2UserInfo iamUserInfoDTO2UserInfo,
      TokenStoreService tokenStoreService,
      AccessTokenBuilderService accessTokenBuilderService){
    this.iamUserInfoDTO2UserInfo = iamUserInfoDTO2UserInfo;
    this.tokenStoreService = tokenStoreService;
    this.accessTokenBuilderService = accessTokenBuilderService;
  }

  @Override
  public UserInfo getUserLoginInfo() {
    IamUserInfoDTO user = SecurityUtils.getPrincipal();
    return iamUserInfoDTO2UserInfo.mapIamUserToUserInfo(user);
  }

  @Override
  public TokenResponse generateAuthUser() {
    final Map<String, Object> attributes = Map.of(
        "sub", "123456",
        "fiscalNumber", "TINIT-PPPPPP01P30P736P",
        "familyName", "Polo",
        "name", "Marco",
        "email", "marco.polo@example.com",
        "iss", "issuer"
    );
    TokenResponse accessToken = new TokenResponse(accessTokenBuilderService.build(),"Bearer",300,null,null);
    IamUserInfoDTO iamUserInfoDTO = IamUserInfoDTO.map2IamUserInfoDTO(attributes);
    tokenStoreService.save(accessToken.getAccessToken(), iamUserInfoDTO);
    return accessToken;
  }


}
