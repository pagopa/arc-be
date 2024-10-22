package it.gov.pagopa.arc.security;

import it.gov.pagopa.arc.config.RedisConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@CacheConfig(cacheNames = RedisConfig.CACHE_OAUTH2_STATE)
public class OAuth2StateStoreRepository {

  @CachePut(key = "#oauth2State")
  public OAuth2AuthorizationRequest save(String oauth2State, OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
    log.info("Cache put for state: {}", oauth2State);
    return oAuth2AuthorizationRequest;
  }
  @Cacheable(unless="#result == null")
  public OAuth2AuthorizationRequest getOAuth2AuthorizationRequest(String oauth2State) {
    log.info("Cache get for state: {}", oauth2State);
    return null;
  }
  @CacheEvict
  public OAuth2AuthorizationRequest delete(String oauth2State){
    log.info("Cache evict for state: {}", oauth2State);
    return null;
  }

}
