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

    public BizEventsConnectorImpl(@Value("${rest-client.biz-events.baseUrl}") String apikey,
                                  BizEventsRestClient bizEventsRestClient) {
        this.apikey = apikey;
        this.bizEventsRestClient = bizEventsRestClient;
    }

    @Override
    public BizEventsTransactionsListDTO transactionsList(String fiscalCode, String continuationToken, int size) {
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = BizEventsTransactionsListDTO.builder()
                .transactions(new ArrayList<>())
                .build();
        try {
            bizEventsTransactionsListDTO = bizEventsRestClient.transactionsList(apikey, fiscalCode, continuationToken, size);
        }catch (FeignException e){
            switch (e.status()){
                case 401 -> log.error("Unauthorized" + e.status());
                case 404 -> {
                    return bizEventsTransactionsListDTO;
                }
                case 429 -> log.error("Too Many Requests" + e.status());
                case 500 -> log.error("Internal Server Error" + e.status());
                default -> log.error("Unexpected Error: " + e.status());
            }
        }
        return bizEventsTransactionsListDTO;
    }


}
