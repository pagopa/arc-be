package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidResponseDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Service
@Slf4j
public class BizEventsPaidNoticeConnectorImpl implements BizEventsPaidNoticeConnector {

    private static final String ERROR_MESSAGE_INVOCATION_EXCEPTION = "An error occurred handling request from biz-Events";
    private final String apikey;
    private final BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient;
    private final ObjectMapper objectMapper;

    public BizEventsPaidNoticeConnectorImpl(@Value("${rest-client.biz-events.paid-notice.api-key}") String apikey,
                                            BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient, ObjectMapper objectMapper) {
        this.apikey = apikey;
        this.bizEventsPaidNoticeRestClient = bizEventsPaidNoticeRestClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public BizEventsPaidResponseDTO getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering) {
        Response response;
        BizEventsPaidResponseDTO bizEventsPaidResponseDTO;

        try {
            response = bizEventsPaidNoticeRestClient.paidNoticeList(apikey, fiscalCode, continuationToken, size, isPayer, isDebtor, orderBy, ordering);
            if (response.status() != HttpStatus.OK.value()){
                throw FeignException.errorStatus("getPaidNoticeList", response);
            }
            bizEventsPaidResponseDTO = extractBizEventsPaidNoticeListDTOAndHeaderFromFeignResponse(response);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()){
              bizEventsPaidResponseDTO = handleNotFound();

              log.info("A {} occurred while handling request getPaidNoticeList from biz-Events: HttpStatus {} - {}",
                        e.getClass(),
                        e.status(),
                        e.getMessage());

                return bizEventsPaidResponseDTO;
            }else {
                throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
            }
        }

        return bizEventsPaidResponseDTO;
    }

    private BizEventsPaidResponseDTO handleNotFound(){
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = BizEventsPaidNoticeListDTO.builder()
                .notices(new ArrayList<>())
                .build();

        return BizEventsPaidResponseDTO.builder()
                .continuationToken(null)
                .notices(bizEventsPaidNoticeListDTO.getNotices())
                .build();
    }

    private BizEventsPaidResponseDTO extractBizEventsPaidNoticeListDTOAndHeaderFromFeignResponse(Response response){
        String continuationTokenHeader = null;
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO;

        if(response.body() == null){
            throw new BizEventsInvocationException("Error during processing: response body is null");
        }

        try {
            bizEventsPaidNoticeListDTO = objectMapper.readValue(response.body().asInputStream(), BizEventsPaidNoticeListDTO.class);
        } catch (IOException e) {
            throw new BizEventsInvocationException("Error reading or deserializing the response body");
        }

        if(!response.headers().isEmpty()){
            continuationTokenHeader = response.headers()
                    .getOrDefault("x-continuation-token", Collections.emptyList())
                    .stream()
                    .findFirst()
                    .orElse(null);
        }
        return BizEventsPaidResponseDTO.builder().notices(bizEventsPaidNoticeListDTO.getNotices()).continuationToken(continuationTokenHeader).build();
    }

}
