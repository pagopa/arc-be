package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import it.gov.pagopa.arc.controller.generated.ArcZendeskAssistanceApi;
import it.gov.pagopa.arc.security.CustomAuthenticationSuccessHandler;
import it.gov.pagopa.arc.security.CustomLogoutHandler;
import it.gov.pagopa.arc.security.CustomLogoutSuccessHandler;
import it.gov.pagopa.arc.service.AccessTokenValidationService;
import it.gov.pagopa.arc.service.AuthService;
import it.gov.pagopa.arc.service.TokenStoreService;
import it.gov.pagopa.arc.service.ZendeskAssistanceTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({ArcAuthApi.class, ArcZendeskAssistanceApi.class})
@Import(OAuth2LoginConfig.class)
class OAuth2LoginConfigTest {

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepositoryMock;
    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandlerMock;

    @MockitoBean
    private CustomLogoutHandler customLogoutHandler;
    @MockitoBean
    JWTConfiguration jwtConfiguration;
    @MockitoBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @MockitoBean
    private ZendeskAssistanceTokenService zendeskAssistanceTokenServiceMock;
    @MockitoBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockitoBean
    private TokenStoreService tokenStoreService;
    @MockitoBean
    AccessTokenValidationService accessTokenValidationService;

    @MockitoBean
    AuthorizationRequestRepository authorizationRequestRepository;

    @Test
    void givenURLWithoutCodeAndStateWhenWithoutAccessTokenThenRedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/token/oneidentity"))
                .andExpect(status().is(400));
    }

    @Test
    void givenURLWhenWithoutAccessTokenThenRedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/token/oneidentity?code=fakeCode&state=fakeState"))
                .andExpect(status().is(400));
    }

    @Test
    void givenSecurityURLWhenCallEndpointThenNoRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/token/assistance")
                        .param("userEmail", "someone@email.com"))
                .andExpect(status().is4xxClientError());
    }

}