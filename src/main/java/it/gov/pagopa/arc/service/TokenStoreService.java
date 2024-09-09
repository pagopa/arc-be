package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Optional;


public interface TokenStoreService {

  void save(String accessToken, IamUserInfoDTO userInfo);

  Optional<IamUserInfoDTO> getUserInfo(String accessToken);

  Optional<IamUserInfoDTO> delete(String accessToken);

}
