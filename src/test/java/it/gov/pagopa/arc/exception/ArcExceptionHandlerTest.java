package it.gov.pagopa.arc.exception;


import it.gov.pagopa.arc.exception.custom.InternalServerException;
import it.gov.pagopa.arc.exception.custom.TooManyRequestException;
import it.gov.pagopa.arc.exception.custom.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.Mockito.doThrow;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(value = {ArcExceptionHandlerTest.TestController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {
        ArcExceptionHandlerTest.TestController.class,
        ArcExceptionHandler.class})
class ArcExceptionHandlerTest {

    public static final String DATA = "data";
    public static final String HEADER = "header";
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestController testControllerSpy;

    @RestController
    static class TestController {

        @GetMapping("/test")
        void testEndpoint(@RequestHeader(HEADER) String Header, @RequestParam(DATA) String data) {
        }
    }

    @Test
    void handleMissingServletRequestParameterException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER, HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Required request parameter 'data' for method parameter type String is not present"));

    }

    @Test
    void handleMissingRequestHeaderExceptions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Required request header 'header' for method parameter type String is not present"));
    }

    @Test
    void handleUnauthorizedError() throws Exception {
        doThrow(new UnauthorizedException("Error")).when(testControllerSpy).testEndpoint(HEADER,DATA);

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER,HEADER)
                        .param(DATA, DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("unauthorized"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));
    }

    @Test
    void handleTooManyRequestException() throws Exception {
        doThrow(new TooManyRequestException("Error")).when(testControllerSpy).testEndpoint(HEADER,DATA);

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER,HEADER)
                        .param(DATA, DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isTooManyRequests())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("too_many_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));
    }

    @Test
    void handleInternalServerException() throws Exception {
        doThrow(new InternalServerException("Error")).when(testControllerSpy).testEndpoint(HEADER,DATA);

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER,HEADER)
                        .param(DATA, DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("internal_server_error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));
    }
}