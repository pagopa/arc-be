package it.gov.pagopa.arc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
@EnableCaching
public class RedisConfig {

  public static final String CACHE_NAME_ACCESS_TOKEN = "ACCESS_TOKEN";

  public static final String CACHE_OAUTH2_STATE = "OAUTH2_STATE";

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
      ObjectMapper objectMapper,
      @Value("${jwt.access-token.expire-in}") int accessTokenExpirationSeconds
  ) {
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    cacheConfigurations.put(CACHE_OAUTH2_STATE,redisConfiguration(objectMapper,300,CACHE_OAUTH2_STATE));
    cacheConfigurations.put(CACHE_NAME_ACCESS_TOKEN,redisConfiguration(objectMapper,accessTokenExpirationSeconds,CACHE_NAME_ACCESS_TOKEN));
    return builder -> builder
        .withInitialCacheConfigurations(cacheConfigurations);
  }

  private RedisCacheConfiguration redisConfiguration( ObjectMapper objectMapper,int ttl, String cacheType){
    Class<?> type;

    if(cacheType.equals(CACHE_NAME_ACCESS_TOKEN)){
      type = IamUserInfoDTO.class;
    } else {
      type = OAuth2AuthorizationRequest.class;
    }

    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper,type)))
        .entryTtl(Duration.ofSeconds(ttl))
        .disableCachingNullValues();
  }

}
