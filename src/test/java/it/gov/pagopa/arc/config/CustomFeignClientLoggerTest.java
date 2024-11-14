package it.gov.pagopa.arc.config;

import feign.Request;
import feign.Response;
import feign.Util;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CustomFeignClientLoggerTest {
    private static final String CONFIG_KEY = "ExampleClass#exampleMethod(String p)";
    private final CustomFeignClientLogger customFeignClientLogger = new CustomFeignClientLogger();
    private Request request;
    private MemoryAppender memoryAppender;
    @BeforeEach
    void setUp() {
         request = Request.create(
                Request.HttpMethod.GET,
                "https://api.example.com/test",
                new HashMap<>(),
                null,
                StandardCharsets.UTF_8,
                null
        );

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.config.CustomFeignClientLogger");
        memoryAppender = new MemoryAppender();
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenRequestWhenLogRequestThenLogNothing() {
        //given
        //when
        customFeignClientLogger.logRequest(CONFIG_KEY, feign.Logger.Level.FULL, request);
        //then
        assertEquals(0, memoryAppender.getLoggedEvents().size());

    }

    @Test
    void givenResponseWhenLogAndRebufferResponseThenLog() throws IOException {
        //given
        String responseBody = "{\"notices\": []}";
        Response response = Response.builder()
                .status(200)
                .reason("OK")
                .request(request)
                .body(responseBody, StandardCharsets.UTF_8)
                .build();
        //when
        Response clonedResponse = customFeignClientLogger.logAndRebufferResponse(CONFIG_KEY, feign.Logger.Level.FULL, response, 100);
        //then
        byte[] bodyData = Util.toByteArray(clonedResponse.body().asInputStream());
        String responseString = new String(bodyData, StandardCharsets.UTF_8);

        assertEquals(2, memoryAppender.getLoggedEvents().size());
        assertEquals(responseBody, responseString);
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_REQUEST] ---> GET https://api.example.com/test"));
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(1).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_RESPONSE] <--- Status: 200, response reason: OK, elapsed time (100 ms)"));
    }

    @Test
    void givenErrorResponseWhenLogAndRebufferResponseThenLogError() throws IOException {
        //given
        String responseBody = "{\"error\":\"No records found for the requested user\"}";
        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(request)
                .body(responseBody, StandardCharsets.UTF_8)
                .build();
        //when
        Response clonedResponse = customFeignClientLogger.logAndRebufferResponse(CONFIG_KEY, feign.Logger.Level.FULL, response, 100);
        //then
        byte[] bodyData = Util.toByteArray(clonedResponse.body().asInputStream());
        String responseString = new String(bodyData, StandardCharsets.UTF_8);

        assertEquals(2, memoryAppender.getLoggedEvents().size());
        assertEquals(responseBody, responseString);
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_REQUEST] ---> GET https://api.example.com/test"));
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(1).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_RESPONSE] <--- Status: 404, response reason: Not Found, elapsed time (100 ms), response: {\"error\":\"No records found for the requested user\"}"));
    }

    @Test
    void givenEmptyBodyWhenLogAndRebufferResponseThenLog() throws IOException {
        //given
        Response response = Response.builder()
                .status(200)
                .reason("OK")
                .request(request)
                .build();
        //when
        Response clonedResponse = customFeignClientLogger.logAndRebufferResponse(CONFIG_KEY, feign.Logger.Level.FULL, response, 100);
        //then

        assertEquals(2, memoryAppender.getLoggedEvents().size());

        assertNull(clonedResponse.body());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_REQUEST] ---> GET https://api.example.com/test"));
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(1).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_RESPONSE] <--- Status: 200, response reason: OK, elapsed time (100 ms)"));
    }

    @Test
    void givenEmptyBodyAnd400ErrorWhenLogAndRebufferResponseThenLog() throws IOException {
        //given
        Response response = Response.builder()
                .status(400)
                .reason("BAD REQUEST")
                .request(request)
                .build();
        //when
        Response clonedResponse = customFeignClientLogger.logAndRebufferResponse(CONFIG_KEY, feign.Logger.Level.FULL, response, 100);
        //then

        assertEquals(2, memoryAppender.getLoggedEvents().size());

        assertNull(clonedResponse.body());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_REQUEST] ---> GET https://api.example.com/test"));
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(1).getFormattedMessage().contains("[ExampleClass#exampleMethod] [FEIGN_CLIENT_RESPONSE] <--- Status: 400, response reason: BAD REQUEST, elapsed time (100 ms)"));
    }

    @Test
    void givenConfigKeyAndFormatStringWhenLogThenReturnLogInfo() {
        //given
        String formatString = "This is an example string %s";
        //when
        customFeignClientLogger.log(CONFIG_KEY, formatString, "Arg1");
        //then
        assertEquals(1, memoryAppender.getLoggedEvents().size());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[ExampleClass#exampleMethod] This is an example string Arg1"));
    }
}