package it.gov.pagopa.arc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.gov.pagopa.arc.config.OAuth2LoginConfig;
import it.gov.pagopa.arc.controller.generated.ArcTransactionsApi;
import it.gov.pagopa.arc.service.TransactionsService;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@WebMvcTest(value = {
    ArcTransactionsApi.class
})
@Import(OAuth2LoginConfig.class)
class AuthenticationController {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionsService transactionsServiceMock;

  @Test
  void givenAuthenticationEndpointGetARedirectToIDPLoginPage() throws Exception {
    MvcResult result = mockMvc.perform(
            get("/login/oneidentity")
        ).andExpect(status().is3xxRedirection())
        .andReturn();

    Assertions.assertNotNull( result.getResponse().getRedirectedUrl() );

    UriComponents uriComponents = UriComponentsBuilder.fromUriString( result.getResponse().getRedirectedUrl()).build() ;
    Map<String, String> queryParams = uriComponents.getQueryParams()
        .toSingleValueMap();
    Assertions.assertNotNull(queryParams.get("client_id"));
    Assertions.assertNotNull(queryParams.get("scope"));
    Assertions.assertNotNull(queryParams.get("state"));
    Assertions.assertNotNull(queryParams.get("redirect_uri"));
    Assertions.assertNotNull(queryParams.get("nonce"));
  }

}
