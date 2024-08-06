package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.service.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2LoginConfig {

  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  OAuth2LoginConfig(
      CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .oauth2Login(oauth2Login -> oauth2Login
            .authorizationEndpoint(authConfig ->
                authConfig
                    .baseUri("/login")
            )
            .redirectionEndpoint(redirection ->
                redirection
                    .baseUri("/token/oneidentity")
            )
            .successHandler(customAuthenticationSuccessHandler)
        )
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll());
    return http.build();
  }

}
