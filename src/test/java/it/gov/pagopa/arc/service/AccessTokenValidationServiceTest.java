package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ch.qos.logback.classic.LoggerContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.config.JWTConfiguration.AccessToken;
import it.gov.pagopa.arc.utils.MemoryAppender;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class AccessTokenValidationServiceTest {

  private AccessTokenValidationService accessTokenValidationService;
  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

  private MemoryAppender memoryAppender;

  @BeforeEach
  public void setUp() throws Exception {
    KeyPair keyPair = genKeyPair();
    publicKey = (RSAPublicKey) keyPair.getPublic();
    privateKey = (RSAPrivateKey) keyPair.getPrivate();
    AccessToken accessToken = new AccessToken(3600,Base64.getEncoder().encodeToString(privateKey.getEncoded()),Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    JWTConfiguration jwtConfiguration = new JWTConfiguration("audience","Bearer",accessToken);
    accessTokenValidationService = new AccessTokenValidationService(jwtConfiguration);

    ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.service.AccessTokenValidationService");
    memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(ch.qos.logback.classic.Level.INFO);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
  }

  @Test
  void testValidateTokenSuccess() {
    assertDoesNotThrow(() -> accessTokenValidationService.validate(genToken(publicKey,privateKey,3600)));
  }

  @Test
  void testValidateTokenExpired() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    String token = genToken(publicKey,privateKey,-1);
    accessTokenValidationService.validate(token);
    Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("Provided token is expired: "));

  }

  @Test
  void testValidateSignatureInvalid()
      throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    KeyPair newKeyPair = genKeyPair();
    accessTokenValidationService.validate(genToken((RSAPublicKey) newKeyPair.getPublic(),(RSAPrivateKey) newKeyPair.getPrivate(),3600));
    Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("Provided signature is invalid: "));
  }

  private String genToken(RSAPublicKey publicKey,RSAPrivateKey privateKey,int ttl){
    return JWT.create()
        .withClaim("typ","Bearer" )
        .withClaim("sub","_7284fdec21b65e716223feeb9b3564c1")
        .withClaim("familyName","Polo")
        .withClaim("name","Marco")
        .withClaim("fiscalNumber","TINIT-PLOMRC01P30L736Y")
        .withClaim("email","ilmilione@virgilio.it")
        .withClaim("aud","mockAudience")
        .withClaim("nonce","nonce")
        .withIssuer("audience")
        .withJWTId(UUID.randomUUID().toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plusSeconds(ttl))
        .sign(Algorithm.RSA512(publicKey, privateKey));
  }

  private KeyPair genKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048); // You can choose the key size
    return keyGen.generateKeyPair();
  }

}