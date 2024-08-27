package it.gov.pagopa.arc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcZendeskAssistanceApi;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.ZendeskAssistanceTokenService;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {
        ArcZendeskAssistanceApi.class
})
@AutoConfigureMockMvc(addFilters = false)
class AssistanceControllerImplTest {

    private final static String FAKE_USER_EMAIL = "someone@email.com";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilterMock;
    @MockBean
    ZendeskAssistanceTokenService zendeskAssistanceTokenServiceMock;

    @Test
    void givenUserEmailWhenGetZendeskAssistanceTokenThenReturnZendeskAssistanceToken() throws Exception {
        //given
        String assistanceToken = "fakeAssistanceToken";
        String returnTo = "help-center.com";

        ZendeskAssistanceTokenResponse expected = ZendeskAssistanceTokenResponse
                .builder()
                .assistanceToken(assistanceToken)
                .returnTo(returnTo)
                .build();

        Mockito.when(zendeskAssistanceTokenServiceMock.retrieveZendeskAssistanceTokenResponse(FAKE_USER_EMAIL)).thenReturn(expected);
        //when
        MvcResult result = mockMvc.perform(
                        get("/token/assistance")
                                .param("userEmail", FAKE_USER_EMAIL)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        ZendeskAssistanceTokenResponse resultResponse = TestUtils.objectMapper.readValue(result.getResponse().getContentAsString(),
                ZendeskAssistanceTokenResponse.class);
        //then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(expected,resultResponse);
        Mockito.verify(zendeskAssistanceTokenServiceMock).retrieveZendeskAssistanceTokenResponse(any());
    }

    @Test
    void givenNoUserEmailWhenGetZendeskAssistanceTokenThenReturnBadRequest() throws Exception {
        mockMvc.perform(
                        get("/token/assistance")
                ).andExpect(status().is4xxClientError())
                .andReturn();
    }
}
