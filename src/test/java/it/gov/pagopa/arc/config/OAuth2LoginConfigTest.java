package it.gov.pagopa.arc.config;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import it.gov.pagopa.arc.controller.generated.ArcZendeskAssistanceApi;
import it.gov.pagopa.arc.security.CustomLogoutHandler;
import it.gov.pagopa.arc.security.CustomLogoutSuccessHandler;
import it.gov.pagopa.arc.service.AccessTokenValidationService;
import it.gov.pagopa.arc.service.AuthService;
import it.gov.pagopa.arc.security.CustomAuthenticationSuccessHandler;
import it.gov.pagopa.arc.service.TokenStoreService;
import it.gov.pagopa.arc.service.ZendeskAssistanceTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest({ArcAuthApi.class, ArcZendeskAssistanceApi.class})
@Import(OAuth2LoginConfig.class)
class OAuth2LoginConfigTest {

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepositoryMock;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandlerMock;

    @MockBean
    private CustomLogoutHandler customLogoutHandler;
    @MockBean
    JWTConfiguration jwtConfiguration;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @MockBean
    private ZendeskAssistanceTokenService zendeskAssistanceTokenServiceMock;
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private TokenStoreService tokenStoreService;
    @MockBean
    AccessTokenValidationService accessTokenValidationService;

    @MockBean
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