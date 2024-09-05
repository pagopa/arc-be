package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Map;
import java.util.Optional;
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
  public Optional<IamUserInfoDTO> getUserInfo(String accessToken) {
    return Optional.ofNullable(tokens.get(accessToken));
  }
  @Override
  public Optional<IamUserInfoDTO> delete(String accessToken){
    return Optional.ofNullable(tokens.remove(accessToken));
  }

}
