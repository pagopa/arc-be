package it.gov.pagopa.arc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import it.gov.pagopa.arc.config.JWTConfiguration;
import it.gov.pagopa.arc.utils.CertUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccessTokenValidationService {

  private final JWTConfiguration jwtConfiguration;
  public AccessTokenValidationService(JWTConfiguration jwtConfiguration){
    this.jwtConfiguration = jwtConfiguration;
  }
  public void validate(String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    Algorithm algorithm = Algorithm.RSA512( CertUtils.pemPub2PublicKey(jwtConfiguration.getAccessToken().getPublicKey()), null);
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer(jwtConfiguration.getAudience())
        .build();
    verifier.verify(token);
  }

}
