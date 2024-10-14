package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.security.CustomAuthenticationFailureHandler;
import it.gov.pagopa.arc.security.CustomAuthenticationEntryPoint;
import it.gov.pagopa.arc.security.CustomLogoutHandler;
import it.gov.pagopa.arc.security.CustomLogoutSuccessHandler;
import it.gov.pagopa.arc.security.InMemoryOAuth2AuthorizationRequestRepository;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.security.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig {

  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomLogoutHandler customLogoutHandler;
  OAuth2LoginConfig(
      CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      CustomLogoutHandler customLogoutHandler,
      CustomLogoutSuccessHandler customLogoutSuccessHandler) {
    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.customLogoutHandler = customLogoutHandler;
    this.customLogoutSuccessHandler = customLogoutSuccessHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
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
            .failureHandler(new CustomAuthenticationFailureHandler())
        )
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler(customLogoutSuccessHandler)
        )
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        )

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize -> authorize

            // Actuator endpoints
            .requestMatchers(
                "/actuator",
                "/actuator/**"
            ).permitAll()

            .requestMatchers(
                "/auth/testuser"
            ).permitAll()

            .anyRequest().authenticated());
    return http.build();
  }

}
