package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.security.InMemoryOAuth2AuthorizationRequestRepository;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig {

  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  OAuth2LoginConfig(
      CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .oauth2Login(oauth2Login -> oauth2Login
            .authorizationEndpoint(authConfig ->
                authConfig
                    .baseUri("/login")
                    .authorizationRequestRepository(new InMemoryOAuth2AuthorizationRequestRepository())
            )
            .redirectionEndpoint(redirection ->
                redirection
                    .baseUri("/token/oneidentity*")
            )
            .successHandler(customAuthenticationSuccessHandler)
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize -> authorize

            // endpoint must be protected
            .requestMatchers(
                "/auth",
                    "/auth/*",
                    "/assistance"
            ).authenticated()

            // Should be changed
            .anyRequest().permitAll());
    return http.build();
  }

}
