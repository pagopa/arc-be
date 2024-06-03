package it.gov.pagopa.arc.exception;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(value = {ExceptionHandlerTest.TestController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {
        ExceptionHandlerTest.TestController.class,
        ExceptionHandler.class})
class ExceptionHandlerTest {

    public static final String DATA = "data";
    public static final String HEADER = "header";
    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {

        @GetMapping("/test")
        void testEndpoint(@RequestHeader(HEADER) String header, @RequestParam(DATA) String data) {
        }
    }

    @Test
    void givenRequestWithMissingParameterWhenHandlingMissingServletRequestParameterThenReturnException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER, HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Required request parameter 'data' for method parameter type String is not present"));

    }

    @Test
    void givenRequestWithMissingHeaderWhenHandlingMissingRequestHeaderThenReturnException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Required request header 'header' for method parameter type String is not present"));
    }

}