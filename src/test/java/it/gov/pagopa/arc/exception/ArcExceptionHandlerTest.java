package it.gov.pagopa.arc.exception;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.exception.custom.*;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

import static org.mockito.Mockito.doThrow;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(value = {ArcExceptionHandlerTest.TestController.class})
@ContextConfiguration(classes = {
    ArcExceptionHandlerTest.TestController.class,
    ArcExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class ArcExceptionHandlerTest {

    public static final String DATA = "data";
    public static final String HEADER = "header";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestController testControllerSpy;

    private static final String TRANSACTION_ID = "TRANSACTION_ID";
    private static final String EVENT_ID = "EVENT_ID";
    private MemoryAppender memoryAppender;

    @RestController
    static class TestController {

        @GetMapping("/test")
        void testEndpoint() {
        }

        @GetMapping("/test/{Id}")
        void testEndpointDetails() {
        }
        @GetMapping("/test/{transactionId}/pdf")
        void testEndpointPdf() {
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

    @Test
    void givenRequestWhenBizEventsServiceReturnInvalidAmountThenHandleBizEventsInvalidAmountException() throws Exception {
        doThrow(new BizEventsInvalidAmountException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_amount"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.BizEventsInvalidAmountException occurred handling request GET /test: HttpStatus 400 - Error"));
    }

    @Test
    void givenRequestWhenBizEventsServiceReturnInvalidDateThenHandleBizEventsInvalidDateException() throws Exception {
        doThrow(new BizEventsInvalidDateException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_date"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.BizEventsInvalidDateException occurred handling request GET /test: HttpStatus 400 - Error"));
    }

    @Test
    void givenRequestWhenBizEventsServiceReturnNotFoundErrorThenHandleBizEventsNotFoundExceptionPdfError() throws Exception {
        doThrow(new BizEventsReceiptNotFoundException("Error")).when(testControllerSpy).testEndpointPdf();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/{transactionId}/pdf", TRANSACTION_ID)
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("receipt_not_found_error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.BizEventsReceiptNotFoundException occurred handling request GET /test/TRANSACTION_ID/pdf: HttpStatus 404 - Error"));
    }

    @Test
    void givenRequestWhenPullPaymentReturnBadRequestErrorThenHandlePullPaymentInvalidRequestException() throws Exception {
        doThrow(new PullPaymentInvalidRequestException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.PullPaymentInvalidRequestException occurred handling request GET /test: HttpStatus 400 - Error"));
    }

    @Test
    void givenRequestWhenPullPaymentReturnErrorThenHandlePullPaymentInvocationException() throws Exception {
        doThrow(new PullPaymentInvocationException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("generic_error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.PullPaymentInvocationException occurred handling request GET /test: HttpStatus 500 - Error"));
    }

     @Test
    void givenInvalidTokenThenHandleInvalidTokenException() throws Exception {
        doThrow(new InvalidTokenException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("auth_user_unauthorized"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.InvalidTokenException occurred handling request GET /test: HttpStatus 401 - Error"));
    }

    @Test
    void givenInvalidEmailWhenExtractNameFromEmailAssistanceTokenThenHandleZendeskAssistanceInvalidUserEmailException() throws Exception {
        doThrow(new ZendeskAssistanceInvalidUserEmailException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.ZendeskAssistanceInvalidUserEmailException occurred handling request GET /test: HttpStatus 400 - Error"));
    }

    @Test
    void givenRequestWhenBizEventsServiceReturnNotFoundErrorThenHandleBizEventsPaidNoticeNotFoundExceptionError() throws Exception {
        doThrow(new BizEventsPaidNoticeNotFoundException("Error")).when(testControllerSpy).testEndpointDetails();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/{eventId}", EVENT_ID)
                        .param(DATA, DATA)
                        .header(HEADER,HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("notice_not_found_error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Error"));

        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("A class it.gov.pagopa.arc.exception.custom.BizEventsPaidNoticeNotFoundException occurred handling request GET /test/EVENT_ID: HttpStatus 404 - Error"));
    }

    @Test
    void givenGenericErrorThenThrowException() throws Exception {
        doThrow(new RuntimeException("Error")).when(testControllerSpy).testEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(500))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("generic_error"));
    }

}