package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenStoreServiceImpl implements TokenStoreService{

  private Map<String, IamUserInfoDTO> tokens = new ConcurrentHashMap<>();

  @Override
  public void save(String accessToken, IamUserInfoDTO idTokenClaims) {
    tokens.put(accessToken,idTokenClaims);
  }

  @Override
  public IamUserInfoDTO getUserInfo(String accessToken) {
    return tokens.getOrDefault(accessToken, null);
  }

}
