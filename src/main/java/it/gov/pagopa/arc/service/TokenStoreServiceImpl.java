package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.config.RedisConfig;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.util.Optional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = RedisConfig.CACHE_NAME_ACCESS_TOKEN)
public class TokenStoreServiceImpl implements TokenStoreService{

  @Override
  @CachePut(key = "#accessToken")
  public IamUserInfoDTO save(String accessToken, IamUserInfoDTO idTokenClaims) {
    return idTokenClaims;
  }

  @Override
  @Cacheable(unless="#result == null")
  public IamUserInfoDTO getUserInfo(String accessToken) {
    return null;
  }
  @Override
  @CacheEvict
  public IamUserInfoDTO delete(String accessToken){
    return null;
  }

}
