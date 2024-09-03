package it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken;

import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ZendeskAssistanceTokenResponseMapperTest {
    private final ZendeskAssistanceTokenResponseMapper mapper = Mappers.getMapper(ZendeskAssistanceTokenResponseMapper.class);
    @Test
    void givenParametersWhenToZendeskAssistanceTokenResponseThenReturnZendeskAssistanceTokenResponse() {
        //given
        String assistanceToken = "fakeAssistanceToken";
        String returnTo = "help-center.com";

        ZendeskAssistanceTokenResponse expected = ZendeskAssistanceTokenResponse
                .builder()
                .assistanceToken(assistanceToken)
                .returnTo(returnTo)
                .build();
        //when
        ZendeskAssistanceTokenResponse result = mapper.toZendeskAssistanceTokenResponse(assistanceToken, returnTo);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
    }
}