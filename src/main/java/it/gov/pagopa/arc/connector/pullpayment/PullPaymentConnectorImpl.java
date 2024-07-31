package it.gov.pagopa.arc.connector.pullpayment;

import feign.FeignException;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.exception.custom.PullPaymentInvalidRequestException;
import it.gov.pagopa.arc.exception.custom.PullPaymentInvocationException;
import it.gov.pagopa.arc.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PullPaymentConnectorImpl implements PullPaymentConnector {
    private final String apikey;
    private final PullPaymentRestClient pullPaymentRestClient;

    public PullPaymentConnectorImpl(@Value("${rest-client.pull-payment.api-key}") String apikey,
                                    PullPaymentRestClient pullPaymentRestClient) {
        this.apikey = apikey;
        this.pullPaymentRestClient = pullPaymentRestClient;
    }

    @Override
    public List<PullPaymentNoticeDTO> getPaymentNotices(String fiscalCode, LocalDate dueDate, int limit, int page) {
        List<PullPaymentNoticeDTO> pullPaymentNoticeDTO;
        try{
           pullPaymentNoticeDTO = pullPaymentRestClient.paymentNotices(apikey, fiscalCode, dueDate, limit, page);
        }catch (FeignException e){
            if(e.status() == HttpStatus.NOT_FOUND.value()){
                pullPaymentNoticeDTO = new ArrayList<>();
                log.info("A {} occurred handling request getPaymentNotices from pull-payment: HttpStatus {} - {}",
                        e.getClass(),
                        e.status(),
                        e.getMessage());
            } else if(e.status() == HttpStatus.BAD_REQUEST.value()) {
                throw new PullPaymentInvalidRequestException("One or more inputs provided during the request from pull payment are invalid");
            }else{
                Utilities.logExceptionDetails(e);
                throw new PullPaymentInvocationException("An error occurred handling request from pull payment service");
            }

        }
        return pullPaymentNoticeDTO;
    }
}
