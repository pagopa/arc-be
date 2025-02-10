package it.gov.pagopa.arc.connector.gpd;

import feign.FeignException;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.exception.custom.GPDInvalidRequestException;
import it.gov.pagopa.arc.exception.custom.GPDInvocationException;
import it.gov.pagopa.arc.exception.custom.GPDPaymentNoticeDetailsNotFoundException;
import it.gov.pagopa.arc.exception.custom.GPDTooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GPDConnectorImpl implements GPDConnector {
    private final String apiKey;
    private final GPDRestClient gpdRestClient;

    public GPDConnectorImpl(@Value("${rest-client.gpd.api-key}") String apiKey,
                            GPDRestClient gpdRestClient) {
        this.apiKey = apiKey;
        this.gpdRestClient = gpdRestClient;
    }

    @Override
    public GPDPaymentNoticeDetailsDTO getPaymentNoticeDetails(String userId, String organizationFiscalCode, String iupd) {
        try {
            return gpdRestClient.paymentNoticeDetails(apiKey, organizationFiscalCode, iupd);
        }catch (FeignException e){
            if(e.status() == HttpStatus.NOT_FOUND.value()){
                throw new GPDPaymentNoticeDetailsNotFoundException("An error occurred handling request from GPD to retrieve payment notice with organizationFiscalCode [%s] and iupd [%s] for the current user with userId [%s]".formatted(organizationFiscalCode, iupd, userId));
            } else if(e.status() == HttpStatus.BAD_REQUEST.value()) {
                throw new GPDInvalidRequestException("One or more inputs provided during the request to GPD are invalid");
            } else if (e.status() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                throw new GPDTooManyRequestException("Too many request occurred handling request from GPD");
            }else{
                throw new GPDInvocationException("An error occurred handling request from GPD service");
            }
        }

    }

    @Override
    public GPDPaymentNoticePayloadDTO generatePaymentNotice(String organizationFiscalCode, GPDPaymentNoticePayloadDTO paymentPositionPayload) {
        try {
            return gpdRestClient.createPaymentNotice(apiKey, organizationFiscalCode, true, paymentPositionPayload);
        }catch (FeignException e){
            if(e.status() == HttpStatus.BAD_REQUEST.value()){
                throw new GPDInvalidRequestException("One or more inputs provided during the request to generate payment notice to GPD are invalid");
            } else if (e.status() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                throw new GPDTooManyRequestException("Too many request occurred handling request from GPD");
            } else{
                throw new GPDInvocationException("An error occurred handling request from GPD service");
            }
        }
    }
}
