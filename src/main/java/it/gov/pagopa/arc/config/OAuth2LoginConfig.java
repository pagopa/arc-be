package it.gov.pagopa.arc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2LoginConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .oauth2Login(oauth2Login -> oauth2Login
            .authorizationEndpoint(authConfig -> authConfig.baseUri("/login"))
            .redirectionEndpoint(redirection -> redirection.baseUri("/token/*"))
        )
        .authorizeHttpRequests((authorize) -> authorize
            .anyRequest()
            .permitAll());
    return http.build();
  }

}
