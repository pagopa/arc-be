package it.gov.pagopa.arc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

public class CustomAuthenticationSuccessHandlerTest {

  @Mock
  private AccessTokenBuilderService accessTokenBuilderService;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Authentication authentication;

  @Mock
  private TokenResponse tokenResponse;

  private StringWriter responseWriter;

  @Test
  public void testConstructorInjection() {
    AccessTokenBuilderService accessTokenBuilderService = Mockito.mock(AccessTokenBuilderService.class);
    ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

    CustomAuthenticationSuccessHandler handler = new CustomAuthenticationSuccessHandler(accessTokenBuilderService, objectMapper);

    assertNotNull(handler);
    assertNotNull(handler.accessTokenBuilderService);
    assertNotNull(handler.objectMapper);
  }

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    responseWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(responseWriter);

    when(response.getWriter()).thenReturn(printWriter);
    when(accessTokenBuilderService.build()).thenReturn(tokenResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("accessTokenResponse");
  }

  @Test
  public void testOnAuthenticationSuccess() throws Exception {
    customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

    verify(accessTokenBuilderService).build();
    verify(objectMapper).writeValueAsString(tokenResponse);
    verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    assertEquals("accessTokenResponse", responseWriter.toString().trim());
  }

}