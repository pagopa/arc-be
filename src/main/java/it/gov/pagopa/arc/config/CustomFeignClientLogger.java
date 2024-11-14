package it.gov.pagopa.arc.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

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

        log(configKey,"[FEIGN_CLIENT_REQUEST] ---> %s %s", request.httpMethod(), request.url());

        if(status >= HttpStatus.BAD_REQUEST.value() && response.body() != null){
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            if (bodyData.length > 0) {
                String responseString = new String(bodyData, StandardCharsets.UTF_8);

                log(configKey,"[FEIGN_CLIENT_RESPONSE] <--- Status: %d, response reason: %s, elapsed time (%d ms), response: %s", status, response.reason(), elapsedTime, responseString);
            }
        }else{
            log(configKey,"[FEIGN_CLIENT_RESPONSE] <--- Status: %d, response reason: %s, elapsed time (%d ms)", status, response.reason(), elapsedTime);
        }

        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(methodTag(configKey).concat(format), args));
    }
}