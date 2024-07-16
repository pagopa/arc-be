package it.gov.pagopa.arc.connector.bizevents;

import feign.FeignException;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import it.gov.pagopa.arc.exception.custom.BizEventsReceiptNotFoundException;
import it.gov.pagopa.arc.exception.custom.BizEventsTransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class BizEventsConnectorImpl implements BizEventsConnector {
    private static final String ERROR_MESSAGE_INVOCATION_EXCEPTION = "An error occurred handling request from biz-Events";
    private final String apikey;
    private final  BizEventsRestClient bizEventsRestClient;

    public BizEventsConnectorImpl(@Value("${rest-client.biz-events.api-key}") String apikey,
                                  BizEventsRestClient bizEventsRestClient) {
        this.apikey = apikey;
        this.bizEventsRestClient = bizEventsRestClient;
    }

    @Override
    public BizEventsTransactionsListDTO getTransactionsList(String fiscalCode, int size) {
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO;
        try {
            bizEventsTransactionsListDTO = bizEventsRestClient.transactionsList(apikey, fiscalCode, size);
        }catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()){
                bizEventsTransactionsListDTO =
                    BizEventsTransactionsListDTO
                            .builder()
                            .transactions(new ArrayList<>())
                            .build();

                log.info("A {} occurred handling request getTransactionsList from biz-Events: HttpStatus {} - {}",
                        e.getClass(),
                        e.status(),
                        e.getMessage());
            }else {
                throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
            }
        }
        return bizEventsTransactionsListDTO;
    }

    @Override
    public BizEventsTransactionDetailsDTO getTransactionDetails(String fiscalCode, String transactionId) {
        BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO;
        try {
            bizEventsTransactionDetailsDTO = bizEventsRestClient.transactionDetails(apikey, fiscalCode, transactionId);
        }catch (FeignException e){
            if(e.status() == HttpStatus.NOT_FOUND.value()){
                throw new BizEventsTransactionNotFoundException("An error occurred handling request from biz-Events to retrieve transaction with transaction id [%s] for the current user".formatted(transactionId));
            }
            throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
        }
        return bizEventsTransactionDetailsDTO;
    }

    @Override
    public Resource getTransactionReceipt(String fiscalCode, String transactionId) {
        Resource transactionReceipt;
        try {
            transactionReceipt = bizEventsRestClient.transactionReceipt(apikey, fiscalCode, transactionId);
        }catch (FeignException e){
            if (e.status() == HttpStatus.NOT_FOUND.value()){
                throw  new BizEventsReceiptNotFoundException("An error occurred handling request from biz-Events to retrieve transaction receipt with transaction id [%s] for the current user".formatted(transactionId));
            }
            throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
        }
        return transactionReceipt;
    }
}
