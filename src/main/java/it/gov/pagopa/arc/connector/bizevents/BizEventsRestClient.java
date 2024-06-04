package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "biz-events",
        url = "${biz-events.baseUrl}")
public interface BizEventsRestClient {
    @GetMapping(
            value = "/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    BizEventsTransactionsListDTO transactionsList(
            @RequestHeader("x-api-key") String apikey,
            @RequestHeader("x-fiscal-code") String fiscalCode,
            @RequestHeader("x-continuation-token") String continuationToken,
            @RequestParam("size") int size
    );
}


