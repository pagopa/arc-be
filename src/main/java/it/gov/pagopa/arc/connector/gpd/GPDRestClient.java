package it.gov.pagopa.arc.connector.gpd;

import it.gov.pagopa.arc.config.FeignLoggingConfig;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "gpd",
        url = "${rest-client.gpd.baseUrl}", configuration = FeignLoggingConfig.class)
public interface GPDRestClient {

    @GetMapping(
            value = "//organizations/{organizationFiscalCode}/debtpositions/{iupd}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    GPDPaymentNoticeDetailsDTO paymentNoticeDetails(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @PathVariable(value = "organizationFiscalCode") String organizationFiscalCode,
            @PathVariable(value = "iupd") String iupd);

    @PostMapping(
            value = "//organizations/{organizationFiscalCode}/debtpositions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    GPDPaymentNoticePayloadDTO createPaymentNotice(
            @RequestHeader(value = "Ocp-Apim-Subscription-Key") String apikey,
            @PathVariable(value = "organizationFiscalCode") String organizationFiscalCode,
            @RequestParam(value = "toPublish") Boolean toPublish,
            @RequestBody @Valid GPDPaymentNoticePayloadDTO paymentPositionPayload);

}
