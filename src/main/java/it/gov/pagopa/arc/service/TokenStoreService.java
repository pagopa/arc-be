package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;


public interface TokenStoreService {

  IamUserInfoDTO save(String accessToken, IamUserInfoDTO userInfo);

  IamUserInfoDTO getUserInfo(String accessToken);

  IamUserInfoDTO delete(String accessToken);

}
