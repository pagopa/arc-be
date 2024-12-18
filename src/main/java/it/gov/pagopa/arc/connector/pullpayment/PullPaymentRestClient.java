package it.gov.pagopa.arc.connector.pullpayment;

import it.gov.pagopa.arc.config.FeignLoggingConfig;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "pull-payment",
        url = "${rest-client.pull-payment.baseUrl}", configuration = FeignLoggingConfig.class)
public interface PullPaymentRestClient {
    @GetMapping(
            value = "/payment-notices/v1",
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<PullPaymentNoticeDTO> paymentNotices(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @RequestHeader(value = "x-tax-code") String fiscalCode,
            @RequestParam(value = "dueDate", required = false ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(value = "limit", required = false, defaultValue = "50") int limit,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    );
}
