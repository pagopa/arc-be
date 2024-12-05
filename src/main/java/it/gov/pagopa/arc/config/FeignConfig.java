package it.gov.pagopa.arc.config;

import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeRestClient;
import it.gov.pagopa.arc.connector.gpd.GPDRestClient;
import it.gov.pagopa.arc.connector.pullpayment.PullPaymentRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {PullPaymentRestClient.class, BizEventsPaidNoticeRestClient.class, GPDRestClient.class })
public class FeignConfig {
}
