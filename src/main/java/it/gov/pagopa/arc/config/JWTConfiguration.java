package it.gov.pagopa.arc.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfiguration {

  private String audience;
  private AccessToken accessToken;

  @Getter @Setter @AllArgsConstructor @NoArgsConstructor
  public static class AccessToken {
    private Integer expireIn;
    private String privateKey;
    private String publicKey;
  }

}

