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
    cacheConfigurations.put(CACHE_OAUTH2_STATE,RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(300))
        .disableCachingNullValues());
    cacheConfigurations.put(CACHE_NAME_ACCESS_TOKEN,redisJsonSerializationConfiguration(objectMapper,accessTokenExpirationSeconds,IamUserInfoDTO.class));
    return builder -> builder
        .withInitialCacheConfigurations(cacheConfigurations);
  }

  private RedisCacheConfiguration redisJsonSerializationConfiguration( ObjectMapper objectMapper,int ttl, Class<?> type){
    return RedisCacheConfiguration.defaultCacheConfig()
          .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper,
              (Class<?>) type)))
          .entryTtl(Duration.ofSeconds(ttl))
          .disableCachingNullValues();
  }

}
