package it.gov.pagopa.arc.controller;


import it.gov.pagopa.arc.controller.generated.ArcZendeskAssistanceApi;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import it.gov.pagopa.arc.service.ZendeskAssistanceTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistanceControllerImpl implements ArcZendeskAssistanceApi {
    private final ZendeskAssistanceTokenService zendeskAssistanceTokenService;

    public AssistanceControllerImpl(ZendeskAssistanceTokenService zendeskAssistanceTokenService) {
        this.zendeskAssistanceTokenService = zendeskAssistanceTokenService;
    }

    @Override
    public ResponseEntity<ZendeskAssistanceTokenResponse> getZendeskAssistanceToken(String userEmail) {
        ZendeskAssistanceTokenResponse response = zendeskAssistanceTokenService.retrieveZendeskAssistanceTokenResponse(userEmail);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
