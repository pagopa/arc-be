package it.gov.pagopa.arc.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

class RedisConfigTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final RedisConfig redisConfig = new RedisConfig();

  @Test
  void testCustomizer(){
    // Given
    int expirationSeconds = 10;

    // When
    RedisCacheManagerBuilderCustomizer result = redisConfig.redisCacheManagerBuilderCustomizer(objectMapper, expirationSeconds);

    // Then
    Assertions.assertNotNull(result);

    // When
    RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilderMock = Mockito.mock(RedisCacheManager.RedisCacheManagerBuilder.class);
    ArgumentCaptor<Map<String, RedisCacheConfiguration>> captor = ArgumentCaptor.forClass(Map.class);
    when(redisCacheManagerBuilderMock.withInitialCacheConfigurations(any(Map.class))).thenReturn(redisCacheManagerBuilderMock);

    result.customize(redisCacheManagerBuilderMock);

    // Then
    Mockito.verify(redisCacheManagerBuilderMock).withInitialCacheConfigurations(captor.capture());
    Map<String, RedisCacheConfiguration> capturedConfigurations = captor.getValue();

    Assertions.assertTrue(capturedConfigurations.containsKey(RedisConfig.CACHE_NAME_ACCESS_TOKEN));
    RedisCacheConfiguration config = capturedConfigurations.get(RedisConfig.CACHE_NAME_ACCESS_TOKEN);
    Assertions.assertNotNull(config);
    Assertions.assertFalse(config.getAllowCacheNullValues());
    Assertions.assertEquals("FixedDurationTtlFunction[duration=PT" + expirationSeconds + "S]", config.getTtlFunction().toString());

    Assertions.assertTrue(capturedConfigurations.containsKey(RedisConfig.CACHE_OAUTH2_STATE));
    RedisCacheConfiguration configState = capturedConfigurations.get(RedisConfig.CACHE_OAUTH2_STATE);
    Assertions.assertNotNull(configState);
    Assertions.assertFalse(configState.getAllowCacheNullValues());
    Assertions.assertEquals("FixedDurationTtlFunction[duration=PT" + 5 + "M]", configState.getTtlFunction().toString());

    Mockito.verifyNoMoreInteractions(redisCacheManagerBuilderMock);
  }
}