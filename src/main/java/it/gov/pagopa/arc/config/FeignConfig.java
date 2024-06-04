package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.connector.bizevents.BizEventsRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {BizEventsRestClient.class})
public class FeignConfig {
}
