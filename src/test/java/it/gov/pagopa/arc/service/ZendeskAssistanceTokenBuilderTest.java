package it.gov.pagopa.arc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfigSample;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Base64;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class ZendeskAssistanceTokenBuilderTest {
    private static final String USER_EMAIL = "someone@email.com";
    @Mock
    private ZendeskAssistanceTokenConfig zendeskAssistanceTokenMock;

    ZendeskAssistanceTokenBuilder zendeskAssistanceTokenBuilder;

    @BeforeEach
    void setUp() {
        zendeskAssistanceTokenBuilder = new ZendeskAssistanceTokenBuilder(zendeskAssistanceTokenMock);
        IamUserInfoDTO iamUserInfoDTO = IamUserInfoDTOFaker.mockInstance();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                iamUserInfoDTO, null, null);
        authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenUserEmailWhenBuildZendeskAssistanceTokenThenReturnAssistanceToken() {
        //given
        ZendeskAssistanceTokenConfig correctConfiguration = new ZendeskAssistanceTokenConfigSample().getCorrectConfiguration();

        Mockito.when(zendeskAssistanceTokenMock.getTokenType()).thenReturn(correctConfiguration.getTokenType());
        Mockito.when(zendeskAssistanceTokenMock.getAssistanceToken()).thenReturn(correctConfiguration.getAssistanceToken());

        Map<String, String> iamUserInfo = Map.of(
                "aux_data", "FISCAL-CODE789456",
                "name", "name",
                "familyName", "familyName",
                "email", USER_EMAIL
        );

        //when
        String result = zendeskAssistanceTokenBuilder.buildZendeskAssistanceToken(USER_EMAIL);

        DecodedJWT decodedAccessToken = JWT.decode(result);
        String decodedHeader = new String(Base64.getDecoder().decode(decodedAccessToken.getHeader()));
        Map<String, Object> userFields = decodedAccessToken.getClaim("user_fields").asMap();

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("{\"typ\":\"JWT\",\"alg\":\"HS256\"}", decodedHeader);
        Assertions.assertEquals("someone", decodedAccessToken.getClaim("name").asString());
        Assertions.assertEquals(USER_EMAIL, decodedAccessToken.getClaim("email").asString());
        Assertions.assertEquals("_hc_arc", decodedAccessToken.getClaim("organization").asString());
        Assertions.assertEquals("prod-arc", decodedAccessToken.getClaim("product_id").asString());

        Assertions.assertEquals(iamUserInfo.get("aux_data"), userFields.get("aux_data"));
        Assertions.assertEquals(iamUserInfo.get("name"), userFields.get("name"));
        Assertions.assertEquals(iamUserInfo.get("familyName"), userFields.get("familyName"));
        Assertions.assertEquals(iamUserInfo.get("email"), userFields.get("email"));

        Mockito.verify(zendeskAssistanceTokenMock).getTokenType();
        Mockito.verify(zendeskAssistanceTokenMock, Mockito.times(3)).getAssistanceToken();
    }
}