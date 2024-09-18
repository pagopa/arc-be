package it.gov.pagopa.arc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import it.gov.pagopa.arc.exception.custom.InvalidTokenException;
import it.gov.pagopa.arc.fakers.auth.UserInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.ErrorDTO;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(value = {
    ArcAuthApi.class
},  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerImplTest {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AuthService authService;

  @Test
  void givenAuthenticatedUserThenRetrieveUserInfo() throws Exception {
    // given
    UserInfo userInfo = UserInfoDTOFaker.mockInstance();

    Mockito.when(authService.getUserLoginInfo()).thenReturn(userInfo);

    //When
    MvcResult result = mockMvc.perform(
            get("/auth/user")
        ).andExpect(status().is2xxSuccessful())
        .andReturn();

    UserInfo userInfoResponse = objectMapper.readValue(result.getResponse().getContentAsString(),UserInfo.class);

    //then
    Assertions.assertNotNull(userInfoResponse);
    Assertions.assertEquals(userInfo,userInfoResponse);
  }

  @Test
  void givenInvalidAuthenticationThenThrowException() throws Exception {

    Mockito.when(authService.getUserLoginInfo()).thenThrow(InvalidTokenException.class);

    //When
    MvcResult result = mockMvc.perform(
            get("/auth/user")
        ).andExpect(status().is4xxClientError())
        .andReturn();

    ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDTO.class);

    //then
    Assertions.assertNotNull(error);
  }

  @Test
  void getSampleToken() throws Exception {

    Mockito.when(authService.generateAuthUser()).thenReturn(new TokenResponse());

    //When
    MvcResult result = mockMvc.perform(
            get("/auth/testuser")
        ).andExpect(status().is(200))
        .andReturn();

    TokenResponse tokenResponse = objectMapper.readValue(result.getResponse().getContentAsString(), TokenResponse.class);

    //then
    Assertions.assertNotNull(tokenResponse);
  }

}