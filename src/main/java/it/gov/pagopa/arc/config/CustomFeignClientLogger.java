package it.gov.pagopa.arc.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
 public class CustomFeignClientLogger extends Logger {

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        /*
         * This method is intentionally left blank to prevent it
         * from logging the request. We will log both the request and the response
         * in the logAndRebufferResponse method.
         */
    }

    @Override
    protected Response logAndRebufferResponse(
            String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        int status = response.status();
        Request request = response.request();
        String responseString = "";
        byte[] bodyData = new byte[0];

        if (response.body() != null){
            bodyData = Util.toByteArray(response.body().asInputStream());
            if (bodyData.length > 0) {
                responseString = new String(bodyData, StandardCharsets.UTF_8);
            }
        }

        log.info("[FEIGN_CLIENT_REQUEST] ---> {} {}", request.httpMethod(), request.url());
        log.info("[FEIGN_CLIENT_RESPONSE] <--- Status: {}, response reason: {}, elapsed time ({} ms), response: {}", status, response.reason(), elapsedTime, responseString);

        return response.toBuilder().body(bodyData).build();
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(methodTag(configKey).concat(format), args));
    }
}