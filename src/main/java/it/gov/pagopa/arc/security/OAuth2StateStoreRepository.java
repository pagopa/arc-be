package it.gov.pagopa.arc.security;

import it.gov.pagopa.arc.config.RedisConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = RedisConfig.CACHE_OAUTH2_STATE)
public class OAuth2StateStoreRepository {

  @CachePut(key = "#oauth2State")
  public OAuth2AuthorizationRequest save(String oauth2State, OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
    return oAuth2AuthorizationRequest;
  }
  @Cacheable(unless="#result == null")
  public OAuth2AuthorizationRequest get(String oauth2State) {
    return null;
  }
  @CacheEvict
  public OAuth2AuthorizationRequest delete(String oauth2State){
    return null;
  }

}
