package it.gov.pagopa.arc.exception;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;
import utils.MemoryAppender;

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

    private MemoryAppender memoryAppender;

    @RestController
    static class TestController {

        @GetMapping("/test")
        void testEndpoint() {
        }
    }

    @BeforeEach
    void setUp() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.exception.ArcExceptionHandler");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenRequestWhenBizEventsServiceReturnErrorThenHandleBizEventsInvocationException() throws Exception {
        doThrow(new BizEventsInvocationException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("generic_error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.BizEventsInvocationException occurred handling request GET /test: HttpStatus 500 - Error"));
    }

}