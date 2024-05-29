package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
        name = "biz-events",
        url = "${rest-client.biz-events.baseUrl}")
public interface BizEventsRestClient {
    @GetMapping(
            value = "/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    BizEventsTransactionsListDTO transactionsList(
            @RequestHeader("x-api-key") String apikey,
            @RequestHeader("x-fiscal-code") String fiscalCode,
            @RequestHeader("x-continuation-token") String continuationToken,
            @RequestParam("size") int size
    );
}


