package it.gov.pagopa.arc.connector.bizevents;

import feign.FeignException;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class BizEventsConnectorImpl implements BizEventsConnector {
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
                throw new BizEventsInvocationException("An error occurred handling request from biz-Events");
            }
        }
        return bizEventsTransactionsListDTO;
    }

}
