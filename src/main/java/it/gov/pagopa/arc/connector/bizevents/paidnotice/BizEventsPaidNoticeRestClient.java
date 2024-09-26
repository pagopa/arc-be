package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "biz-events-paid-notice",
        url = "${rest-client.biz-events.paid-notice.baseUrl}")
public interface BizEventsPaidNoticeRestClient {

    @GetMapping(
            value = "/paids",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    BizEventsPaidNoticeListDTO paidNoticeList(
                                     @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
                                     @RequestHeader(value = "x-fiscal-code") String fiscalCode,
                                     @RequestHeader(value = "x-continuation-token", required = false) String continuationToken,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "is_payer", required = false) Boolean isPayer,
                                     @RequestParam(value = "is_debtor", required = false) Boolean isDebtor,
                                     @RequestParam(value = "orderby", required = false) String orderBy,
                                     @RequestParam(value = "ordering", required = false) String ordering
    );
}
