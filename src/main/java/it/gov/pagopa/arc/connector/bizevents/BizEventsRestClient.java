package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(
        name = "biz-events",
        url = "${rest-client.biz-events.transactions.baseUrl}")
public interface BizEventsRestClient {
    @GetMapping(
            value = "/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    BizEventsTransactionsListDTO transactionsList(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @RequestHeader(value = "x-fiscal-code") String fiscalCode,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    );

}


