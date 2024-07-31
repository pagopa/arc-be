package it.gov.pagopa.arc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.utils.CertUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenBuilderService {

  private final JWTConfiguration jwtConfiguration;
  private final String allowedAudience;
  private final int expireIn;
  private final RSAPublicKey rsaPublicKey;
  private final RSAPrivateKey rsaPrivateKey;

  public AccessTokenBuilderService(
      JWTConfiguration jwtConfiguration
  ) {
    this.allowedAudience = jwtConfiguration.getAudience();
    this.expireIn = jwtConfiguration.getAccessToken().getExpireIn();
    this.jwtConfiguration = jwtConfiguration;
    try {
      rsaPrivateKey = CertUtils.pemKey2PrivateKey(jwtConfiguration.getAccessToken().getPrivateKey());
      rsaPublicKey = CertUtils.pemPub2PublicKey(jwtConfiguration.getAccessToken().getPublicKey());
    } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
      throw new IllegalStateException("Cannot load private and/or public key", e);
    }
  }

  public String build(){
    Algorithm algorithm = Algorithm.RSA512(rsaPublicKey, rsaPrivateKey);
    return JWT.create()
        .withClaim("typ",jwtConfiguration.getTokenType() )
        .withIssuer(allowedAudience)
        .withJWTId(UUID.randomUUID().toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plusSeconds(expireIn))
        .sign(algorithm);
  }

}
