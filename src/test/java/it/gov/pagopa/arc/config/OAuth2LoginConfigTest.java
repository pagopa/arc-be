package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticesApi;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.AuthService;
import it.gov.pagopa.arc.service.CustomAuthenticationSuccessHandler;
import it.gov.pagopa.arc.service.PaymentNoticesService;
import it.gov.pagopa.arc.service.TokenStoreService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest({ArcAuthApi.class, ArcPaymentNoticesApi.class})
@Import(OAuth2LoginConfig.class)
class OAuth2LoginConfigTest {

  @MockBean
  private ClientRegistrationRepository clientRegistrationRepositoryMock;
  @MockBean
  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandlerMock;
  @MockBean
  private PaymentNoticesService paymentNoticesServiceMock;
  @Mock
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @MockBean
  private AuthService authService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private TokenStoreService tokenStoreService;
  @Test
  void givenURLWithoutCodeAndStateWhenWithoutAccessTokenThenRedirectToLogin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/token/oneidentity"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"));
  }

  @Test
  void givenURLWhenWithoutAccessTokenThenRedirectToLogin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/token/oneidentity?code=fakeCode&state=fakeState"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"));
  }

  @Test
  void givenUnsecurityURLWhenCallEndpointThenNoRedirect() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/payment-notices"))
        .andExpect(status().isOk());
  }

}