package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.config.JWTConfigurationTest;
import java.util.Base64;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccessTokenBuilderServiceTest {

  private AccessTokenBuilderService accessTokenBuilderService;

  @BeforeEach
  void init(){
    JWTConfiguration jwtConfiguration = JWTConfigurationTest.getCorrectConfiguration();
    accessTokenBuilderService = new AccessTokenBuilderService(jwtConfiguration);
  }

  @Test
  void givenInvalidKeyThenThrowException(){
    JWTConfiguration jwtConfiguration = JWTConfigurationTest.getWrongConfiguration();
    assertThrows(IllegalStateException.class,()-> new AccessTokenBuilderService(jwtConfiguration) );
  }

  @Test
  void givenCorrectAccessTokenThenVerifyHeaderAndBodyForRequiredFields(){
    // When
    String token = accessTokenBuilderService.build();

    DecodedJWT decodedAccessToken = JWT.decode(token);
    String decodedHeader = new String(Base64.getDecoder().decode(decodedAccessToken.getHeader()));
    String decodedPayload = new String(Base64.getDecoder().decode(decodedAccessToken.getPayload()));

    Assertions.assertEquals("{\"alg\":\"RS512\",\"typ\":\"JWT\"}", decodedHeader);
    Assertions.assertEquals(JWTConfigurationTest.getExpireIn(), (decodedAccessToken.getExpiresAtAsInstant().toEpochMilli() - decodedAccessToken.getIssuedAtAsInstant().toEpochMilli()) / 1_000);
    Assertions.assertTrue(Pattern.compile("\\{\"typ\":\"Bearer\",\"iss\":\"APPLICATION_AUDIENCE\",\"jti\":\"[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}\",\"iat\":[0-9]+,\"exp\":[0-9]+}").matcher(decodedPayload).matches(), "Payload not matches requested pattern: " + decodedPayload);
  }

}