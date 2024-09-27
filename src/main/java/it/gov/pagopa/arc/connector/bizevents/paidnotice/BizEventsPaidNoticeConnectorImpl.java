package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import feign.Response;
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
    private final ObjectMapper objectMapper;

    public BizEventsPaidNoticeConnectorImpl(@Value("${rest-client.biz-events.paid-notice.api-key}") String apikey,
                                            BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient, ObjectMapper objectMapper) {
        this.apikey = apikey;
        this.bizEventsPaidNoticeRestClient = bizEventsPaidNoticeRestClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering) {
        Response response;
        try {
            response = bizEventsPaidNoticeRestClient.paidNoticeList(apikey, fiscalCode, continuationToken, size, isPayer, isDebtor, orderBy, ordering);

            if (response.status() != HttpStatus.OK.value()){
                throw FeignException.errorStatus("getPaidNoticeList", response);
            }
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()){
                log.info("A {} occurred while handling request getPaidNoticeList from biz-Events: HttpStatus {} - {}",
                        e.getClass(),
                        e.status(),
                        e.getMessage());
                return handleNotFound(e.request());
            }else {
                throw new BizEventsInvocationException(ERROR_MESSAGE_INVOCATION_EXCEPTION);
            }
        }
        return response;
    }

    private Response handleNotFound(Request request){
        Response response;
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = BizEventsPaidNoticeListDTO.builder()
                .notices(new ArrayList<>())
                .build();
        try {

            byte[] serializedObject = objectMapper.writeValueAsBytes(bizEventsPaidNoticeListDTO);
            response = Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .reason("Not Found")
                    .body(serializedObject)
                    .request(request)
                    .build();


        } catch (JsonProcessingException jsonEx) {
            throw new BizEventsInvocationException("Error during response serialization");
        }
        return response;
    }

}
