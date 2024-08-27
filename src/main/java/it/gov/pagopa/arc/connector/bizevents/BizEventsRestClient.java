package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "biz-events",
        url = "${rest-client.biz-events.baseUrl}")
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

    @GetMapping(
            value = "/transactions/{transaction-id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    BizEventsTransactionDetailsDTO transactionDetails(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @RequestHeader(value = "x-fiscal-code") String fiscalCode,
            @PathVariable(value = "transaction-id") String transactionId
    );

    @GetMapping(
            value = "/transactions/{event-id}/pdf")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    Resource transactionReceipt(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @RequestHeader(value = "x-fiscal-code") String fiscalCode,
            @PathVariable(value = "event-id") String transactionId
    );

}


