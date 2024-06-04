package it.gov.pagopa.arc.connector.bizevents;

import feign.FeignException;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class BizEventsConnectorImpl implements BizEventsConnector {
    private final String apikey;
    private final  BizEventsRestClient bizEventsRestClient;

    public BizEventsConnectorImpl(@Value("${apiKey.biz-events}") String apikey,
                                  BizEventsRestClient bizEventsRestClient) {
        this.apikey = apikey;
        this.bizEventsRestClient = bizEventsRestClient;
    }

    @Override
    public BizEventsTransactionsListDTO transactionsList(String fiscalCode, String continuationToken, int size) {
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO;
        try {
            bizEventsTransactionsListDTO = bizEventsRestClient.transactionsList(apikey, fiscalCode, continuationToken, size);
        }catch (FeignException e) {
            bizEventsTransactionsListDTO =
                    BizEventsTransactionsListDTO
                            .builder()
                            .transactions(new ArrayList<>())
                            .build();
        }
        return bizEventsTransactionsListDTO;
    }

}
