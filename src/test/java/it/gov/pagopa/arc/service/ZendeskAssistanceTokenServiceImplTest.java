package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.dto.mapper.zendeskassistancetoken.ZendeskAssistanceTokenResponseMapper;
import it.gov.pagopa.arc.model.generated.ZendeskAssistanceTokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ZendeskAssistanceTokenServiceImplTest {

    private static final String FAKE_USER_EMAIL = "someone@email.com";

    @Mock
    private ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilderMock;
    @Mock
    private ZendeskAssistanceTokenConfig zendeskAssistanceTokenMock;
    @Mock
    private ZendeskAssistanceTokenResponseMapper zendeskAssistanceTokenResponseMapperMock;

    ZendeskAssistanceTokenService zendeskAssistanceTokenService;

    @BeforeEach
    void setUp() {
        zendeskAssistanceTokenService = new ZendeskAssistanceTokenServiceImpl(zendeskAssistanceTokenBuilderMock, zendeskAssistanceTokenMock, zendeskAssistanceTokenResponseMapperMock);
    }

    @Test
    void givenUserEmailWhenRetrieveZendeskAssistanceTokenResponseThenReturn() {
        //given
        String assistanceToken = "FAKE_ASSISTANCE_TOKEN";
        String returnTo = "FAKE_RETURN_TO_URL";
        String actionUrl = "FAKE_ZENDESK_ACTION_URL";

        ZendeskAssistanceTokenResponse assistanceTokenResponse = ZendeskAssistanceTokenResponse.builder()
                .assistanceToken(assistanceToken)
                .returnTo(returnTo)
                .actionUrl(actionUrl)
                .build();

        Mockito.when(zendeskAssistanceTokenBuilderMock.buildZendeskAssistanceToken(FAKE_USER_EMAIL)).thenReturn(assistanceToken);
        Mockito.when(zendeskAssistanceTokenMock.getReturnTo()).thenReturn(returnTo);
        Mockito.when(zendeskAssistanceTokenMock.getActionUrl()).thenReturn(actionUrl);
        Mockito.when(zendeskAssistanceTokenResponseMapperMock.toZendeskAssistanceTokenResponse(assistanceToken, returnTo, actionUrl)).thenReturn(assistanceTokenResponse);
        //when
        ZendeskAssistanceTokenResponse result = zendeskAssistanceTokenService.retrieveZendeskAssistanceTokenResponse(FAKE_USER_EMAIL);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(assistanceToken, result.getAssistanceToken());
        Assertions.assertEquals(returnTo, result.getReturnTo());

        Mockito.verify(zendeskAssistanceTokenBuilderMock).buildZendeskAssistanceToken(anyString());
        Mockito.verify(zendeskAssistanceTokenMock).getReturnTo();
        Mockito.verify(zendeskAssistanceTokenMock).getActionUrl();
        Mockito.verify(zendeskAssistanceTokenResponseMapperMock).toZendeskAssistanceTokenResponse(anyString(), anyString(), anyString());
    }
}