package it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken;

import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZendeskAssistanceTokenResponseMapper {
    default ZendeskAssistanceTokenResponse toZendeskAssistanceTokenResponse(String assistanceToken, String returnTo, String actionUrl){
        ZendeskAssistanceTokenResponse response = new ZendeskAssistanceTokenResponse();
        response.setAssistanceToken(assistanceToken);
        response.setReturnTo(returnTo);
        response.setActionUrl(actionUrl);

        return response;
    }
}
