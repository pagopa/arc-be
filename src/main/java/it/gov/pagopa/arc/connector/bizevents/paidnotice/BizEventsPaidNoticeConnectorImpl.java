package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import feign.FeignException;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class BizEventsPaidNoticeConnectorImpl implements BizEventsPaidNoticeConnector {

    private static final String ERROR_MESSAGE_INVOCATION_EXCEPTION = "An error occurred handling request from biz-Events";
    private final String apikey;
    private final BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient;

    public BizEventsPaidNoticeConnectorImpl(@Value("${rest-client.biz-events.paid-notice.api-key}") String apikey,
                                            BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient) {
        this.apikey = apikey;
        this.bizEventsPaidNoticeRestClient = bizEventsPaidNoticeRestClient;
    }

    @Override
    public BizEventsPaidNoticeListDTO getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering) {
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO;
        try{
            bizEventsPaidNoticeListDTO = bizEventsPaidNoticeRestClient.paidNoticeList(apikey, fiscalCode, continuationToken, size, isPayer, isDebtor, orderBy, ordering);
        }catch (FeignException e){
            if(e.status() == HttpStatus.NOT_FOUND.value()){
                bizEventsPaidNoticeListDTO = BizEventsPaidNoticeListDTO.builder()
                        .paidNoticeList(new ArrayList<>())
                        .build();

                log.info("A {} occurred handling request getNoticesList from biz-Events: HttpStatus {} - {}",
                        e.getClass(),
                        e.status(),
                        e.getMessage());
            }else {
                throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
            }
        }
        return bizEventsPaidNoticeListDTO;
    }
}
