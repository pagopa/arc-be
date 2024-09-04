package it.gov.pagopa.arc.security;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static it.gov.pagopa.arc.config.WireMockConfig.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.service.TokenStoreService;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@ContextConfiguration( initializers = WireMockConfig.WireMockInitializer.class )
@TestPropertySource(
    properties = {
        "spring.security.oauth2.client.registration.oneidentity.provider=oneidentity",
        "spring.security.oauth2.client.registration.oneidentity.client-id=clientid",
        "spring.security.oauth2.client.registration.oneidentity.client-secret=secret",
        "spring.security.oauth2.client.registration.oneidentity.authorization-grant-type=authorization_code",
        "spring.security.oauth2.client.registration.oneidentity.redirect-uri=http://idp/callback",
        "spring.security.oauth2.client.registration.oneidentity.scope=openid",
        //"spring.security.oauth2.client.provider.oneidentity.issuer-uri=http://idp",
        "spring.security.oauth2.client.provider.oneidentity.authorization-uri=http://idp/login",
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX+"spring.security.oauth2.client.provider.oneidentity.token-uri=idp/oidc/token",
        "spring.security.oauth2.client.provider.oneidentity.user-name-attribute=sub",
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX+"spring.security.oauth2.client.provider.oneidentity.jwk-set-uri=idp/oidc/keys",
        "jwt.audience=application",
        "jwt.tokenType=Bearer",
        "jwt.access-token.expire-in=3600",
        "jwt.access-token.private-key=-----BEGIN PRIVATE KEY----- MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYGJCFUvgUr20I /H2MLqtFjKX8IZ6VbxiNZD/I/U2zWhYnGffi6QAeecB8RIZf3rt0fdwZ2l6UZ914 mTnsGfE6yk1ee4wmRovUYpDq2O/lApU1TFPaLx0OOSH3kxFJhFnYaQtmBBUDsBrs PSjj9qzq4R0WcUAs0lYE3CJ8BhDA5U0x2EWSeXM9BySL79xZ0g/XhpDs4ngjXfTT o9RTUf3tfuhECTYa8LCddXUqGiBwDACK9vISWm12JKeV03Mp4/WQxX0iB52KSQBV UTHul3msZx1z6s1FcbgtiQnQgDGXMNwWcfJpZFaedTRL3ZDnSghgko1BI0guTmVj wJlYPF4bAgMBAAECggEAA91/8rtwjYoFwdg00pavCJXx8+3gy1hm7dTx4Ag77MZp 0LWSvKQCOkQK1b2iEpak+elm6gtIIwpesP1n4O2p2T4h6DhIkAJz9EJK/4Ti19WQ eCnH6cAPw3hFOjb1FgK0i9DjlsScyhq0HHPTcbOnolJ1PEhFgr4XrIjxoWhADb7b /pdTJBHwNcBAFtz3LPJNR1d5sE0lGz+P7OwP/jqoDK8LtQqS0Yl1eIOiK/d7zD8H ZV928xt3/K4xapif1FWU7mAkpI0USjoeXJF4ZHP8BhhuBnOwswl6QEZ+S+l3daDd Avcr5Cv97qh9Rolz3ncqH8KMjuFfGBIBAwOYFo9wDQKBgQDWTNRuHcKXSlK7Rar/ aVDs1kvExT3BNf2oaqEI+RQUlhDkbDuWoo093J/xaJHNSCRjLFWVLSIftXVnbCDi HD2XmCHRExpWGw4gZDVNBcLgZh8UWgKlLWYz3Q2etLgG1jNgor/A+86d4WPh+aQX zqtozHXlSecaXywyavB1bTlfbQKBgQC1sRbxS4Ir2M+kW4yPyrIihYhqRwZ8nJOv 3IYmiHwex4t3fpwS4z/dH4B8XNSF68E+KbMRB5YNBNOCwCvGx82s6Bq3P8WR74NV k4bhdn2uPku0u+Gteij8A4ONyL1GBbmFn3l/OmsNQTD+2prcexBuGKh7K7yus66F gph7gfLWpwKBgF1xuPufLHfN589TLKIcqTXsp7NQkoIKaeYjQL7p5XCokwsXitA/ Zzk/V9rrTxBlUcCQ12yp9oQ/GseTJa+SwuS0aKKDIuvC9mD3cSp5xaUVwp2cNiUS a8tXq5W1lb0db9/Gd7jN1CWR33zs3zmmW6Xh6dKmbAha0anWaa26h9btAoGAX/N/ vDo2KlW7gn7egml3HYgLfKS5pkFCNVNufRcDBXY4DwkL/2WHqo0iW4riqT7RtLRs 3od1FLcBxEEcXUPTOIby5OeGvQUSBLV+O79JrCU18eJu0iB7WGu6o7vpSPto+Eo5 7Zi6RCuzZkOoGNvc12eqQjHc2R4HAnbvc/oydm0CgYEAwCoHnT4wa/I1I1Few/C0 v/T8EIikZv/ahjAtBDyCS/ECPWHT/rNoVACli8qRODc5QsYnbfusKruGdjYFYZrK DoQdpTUjoj8IA7SrwiifgPvTo+DAv89Weg6GirhhjvG+9s5rtDsrDMNWpthfs0g5 erTxidoSCP2fjk5BpdnE+Ww= -----END PRIVATE KEY-----",
        "jwt.access-token.public-key=-----BEGIN PUBLIC KEY----- MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmBiQhVL4FK9tCPx9jC6r RYyl/CGelW8YjWQ/yP1Ns1oWJxn34ukAHnnAfESGX967dH3cGdpelGfdeJk57Bnx OspNXnuMJkaL1GKQ6tjv5QKVNUxT2i8dDjkh95MRSYRZ2GkLZgQVA7Aa7D0o4/as 6uEdFnFALNJWBNwifAYQwOVNMdhFknlzPQcki+/cWdIP14aQ7OJ4I13006PUU1H9 7X7oRAk2GvCwnXV1KhogcAwAivbyElptdiSnldNzKeP1kMV9IgedikkAVVEx7pd5 rGcdc+rNRXG4LYkJ0IAxlzDcFnHyaWRWnnU0S92Q50oIYJKNQSNILk5lY8CZWDxe GwIDAQAB -----END PUBLIC KEY-----",
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "spring.security.oauth2.client.provider.oneidentity.issuer-uri=idp",
        "rest-client.pull-payment.api-key=x_api_key0",
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "rest-client.pull-payment.baseUrl=pullPaymentMock",
        "spring.application.name=app",
        "spring.application.version=1",
        "rest-client.fake-fiscal-code=HSLZYB90L59D030S"
    })
@AutoConfigureMockMvc
class IdpIntegrationTest {
    private static final String LOGIN_URL = "/login/oneidentity";
    private static final String TOKEN_URL = "/token/oneidentity";
    private static final String IDP_KEYS_URL = "/idp/oidc/keys";
    private static final String IDP_TOKEN_URL = "/idp/oidc/token";
    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private Environment environment;
    @MockBean
    private TokenStoreService tokenStoreService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenLoginActionThenGetNewState() throws Exception {

        MvcResult result = mockMvc.perform(get(LOGIN_URL))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MultiValueMap<String,String> queryParams = extractQueryParams(result);

        Assertions.assertNotNull(queryParams.get("nonce").get(0));
        Assertions.assertNotNull(queryParams.get("state").get(0));
        Assertions.assertEquals(environment.getProperty("spring.security.oauth2.client.registration.oneidentity.client-id"),queryParams.get("client_id").get(0));
        Assertions.assertEquals(environment.getProperty("spring.security.oauth2.client.registration.oneidentity.scope"),queryParams.get("scope").get(0));
        Assertions.assertEquals(environment.getProperty("spring.security.oauth2.client.registration.oneidentity.redirect-uri"),queryParams.get("redirect_uri").get(0));

    }

    @Test
    void givenValidStateThenRequestAccessToken() throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        String modulusBase64 = Base64.getEncoder().encodeToString(publicKey.getModulus().toByteArray());

        addStubKeys(modulusBase64);

        MvcResult result = mockMvc.perform(get(LOGIN_URL))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MultiValueMap<String,String> queryParams = extractQueryParams(result);
        String idpIdToken = genIdpIdToken(queryParams,publicKey,privateKey);

        addStubToken(idpIdToken);

        MvcResult firstTimeToken = mockMvc.perform(get(TOKEN_URL)
                .param("code","code")
                .param("state", decodeState(queryParams.get("state").get(0)) ))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        TokenResponse token = objectMapper.readValue(firstTimeToken.getResponse().getContentAsString(),TokenResponse.class);

        Assertions.assertNotNull(firstTimeToken);
        Assertions.assertNotNull(token);
        Assertions.assertNotNull(token.getAccessToken());
        Assertions.assertNotNull(token.getTokenType());
        Assertions.assertNotNull(token.getExpiresIn());
    }

    @Test
    void givenInvalidStateThenRequestAccessToken() throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        String modulusBase64 = Base64.getEncoder().encodeToString(publicKey.getModulus().toByteArray());

        addStubKeys(modulusBase64);

        MvcResult result = mockMvc.perform(get(LOGIN_URL))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MultiValueMap<String,String> queryParams = UriComponentsBuilder.newInstance().
            query(result.getResponse().getRedirectedUrl()).build().getQueryParams();
        String idpIdToken = genIdpIdToken(queryParams,publicKey,privateKey);

        addStubToken(idpIdToken);

        MvcResult result1 = mockMvc.perform(get(TOKEN_URL)
                .param("code","code")
                .param("state", UUID.randomUUID().toString() ))
            .andReturn();

        Assertions.assertNotNull(result1);
        Assertions.assertNotEquals(200, result1.getResponse().getStatus());

    }


    @Test
    void givenAlreadyUsedStateThenRequestAccessToken() throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        String modulusBase64 = Base64.getEncoder().encodeToString(publicKey.getModulus().toByteArray());

        addStubKeys(modulusBase64);

        MvcResult result = mockMvc.perform(get(LOGIN_URL))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MultiValueMap<String,String> queryParams = extractQueryParams(result);
        String idpIdToken = genIdpIdToken(queryParams,publicKey,privateKey);

        addStubToken(idpIdToken);

        MvcResult firstTimeToken = mockMvc.perform(get(TOKEN_URL)
                .param("code","code")
                .param("state", decodeState(queryParams.get("state").get(0)) ))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        TokenResponse token = objectMapper.readValue(firstTimeToken.getResponse().getContentAsString(),TokenResponse.class);

        // Expected error cause state has already been used
        MvcResult secondTimeToken = mockMvc.perform(get("/token/oneidentity")
                .param("code","code")
                .param("state", decodeState(queryParams.get("state").get(0)) ))
            .andReturn();

        Assertions.assertNotNull(token);
        Assertions.assertNotNull(firstTimeToken);
        Assertions.assertNotNull(secondTimeToken);
        Assertions.assertNotEquals(200, secondTimeToken.getResponse().getStatus());
    }

    private String genIdpIdToken(MultiValueMap<String,String> m,RSAPublicKey publicKey,RSAPrivateKey privateKey){
        return JWT.create()
            .withClaim("typ","Bearer" )
            .withClaim("sub","_7284fdec21b65e716223feeb9b3564c1")
            .withClaim("familyName","Polo")
            .withClaim("name","Marco")
            .withClaim("fiscalNumber","TINIT-PLOMRC01P30L736Y")
            .withClaim("email","ilmilione@virgilio.it")
            .withClaim("aud","clientid")
            .withClaim("nonce",m.get("nonce").get(0))
            .withIssuer(wireMockServer.baseUrl()+"/idp")
            .withJWTId(UUID.randomUUID().toString())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(3600))
            .sign(Algorithm.RSA256(publicKey, privateKey));
    }
    private void addStubKeys(String modulusBase64) {
        wireMockServer.stubFor(WireMock.get(IDP_KEYS_URL)
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"keys\": [{\"kty\": \"RSA\", \"kid\": \"ce617dc9-83a9-4a4e-b060-2cdf9575f05a\", \"use\": \"sig\", \"alg\": \"RS256\", \"n\": \"" + modulusBase64 + "\", \"e\": \"AQAB\"}]}")));
    }
    private void addStubToken(String token) {
        wireMockServer.stubFor(post(IDP_TOKEN_URL)
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"access_token\": \"Aas6VVQNsQItCgCo6n_R_Cblfj5QBeNj80IlAMYz5gY\", \"token_type\": \"Bearer\", \"expires_in\": 900, \"id_token\": \"" + token + "\"}")));
    }

    private MultiValueMap<String, String> extractQueryParams(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(location).build();
        return uriComponents.getQueryParams();
    }

    private String decodeState(String state) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(state, StandardCharsets.UTF_8.name());
    }

    private static PublicKey getPublicKey() throws Exception {
        String publicKeyPEM = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA01nPztXbqgexWO2hO/yDtkFzXQPQI4GWV5IoN12VgvVNIuY9RGivXDa9XHVrVqOPoaWpY97V8dYPXVNZ2+kXBcxBER3xBDniRHC5pmI5ytMsUkdOF6ajy0KNMZigofsibmzeNlBK4mK+7o8bf0NT4pK89//ZYNlBkWVh3GToTVi0og6+sLEjr5ap0XXKyI0eL7iYhHn3TAM2dZaZYSxF8cD9gR/t6MgYj/FmC2+Ykw840qC1Z3iRFjvHlGjwRQLnT5WV7iBAisHSi5CSpugch5uz+jO73L/okDeMgbOG6wFbcY8jEtpdcIoNLTnIoTLef/AyQja3edHd/vlwBNxupQIDAQAB";
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey() throws Exception {
        String privateKeyPEM = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDTWc/O1duqB7FY7aE7/IO2QXNdA9AjgZZXkig3XZWC9U0i5j1EaK9cNr1cdWtWo4+hpalj3tXx1g9dU1nb6RcFzEERHfEEOeJEcLmmYjnK0yxSR04XpqPLQo0xmKCh+yJubN42UEriYr7ujxt/Q1Pikrz3/9lg2UGRZWHcZOhNWLSiDr6wsSOvlqnRdcrIjR4vuJiEefdMAzZ1lplhLEXxwP2BH+3oyBiP8WYLb5iTDzjSoLVneJEWO8eUaPBFAudPlZXuIECKwdKLkJKm6ByHm7P6M7vcv+iQN4yBs4brAVtxjyMS2l1wig0tOcihMt5/8DJCNrd50d3++XAE3G6lAgMBAAECggEABDsCUEEuQvsPOZ1vNwdsZV2SqoxKq3PYoKjBVGWF6AMgoPa97GyWukuyWZNmi3SZCbwKaoBQjhLvzHBSpC1/I3FSUxnH3Tmc9fTR0xQGngxBI3gNkAJGXkTygslQSYgJwCYHy6bhk7S0zC+47MnQ+BJhyZgJWOlTzaB//lyfbsNy/9rR/4VhlXEc6xlsvoon6RoQEFvR71zwAFYEbBx10VjkGG+AhHDCPH4fc2B4bZJfN+49o7rk1qmMDb8PedOjDshLzdiQkgw1dkELhMhEn9bHw52NVzGIhIEWVhyrCf6gvqmij1lCjZUGveE8qAa5UjWiJ3KF+XOqrMgMtSwkqQKBgQD2NSgWSaBvk7pvhwmBiO31JLnHwLPnKCdS4sJ5p97AvXMPsyyhm0kEi2BJKl7mJ8uCBnE+evLO6/8nHdNSLjAOB7NKvqZQzAGp8NReIkef149HwPv13U3NQqn7lXQJ08uyx7iidKivWmtlfKNNhn9dTBHZ54GpvEGIynajCzbmrQKBgQDbwb/Ger44CvgrDjq4lnKLM35wKulSFd/8VPhJmZYS3SvzDTVQ9Kgr1vGKies+4Dmyp4T180wgoj9i7V2kqZ98CVUg8WORBTisxN85D6RjKHlIFCI5ngHyF2ZUnjAObjvyjdf241qFJJuj5U9ZYgJv7I6+g0N1KPyGYPQ51Ok+2QKBgHKdilzdx6dJoVf8CCvaP9SIVUgtaFKq+at3Tsttn6AgUak0YwmUjahk7d0BsY35Zp7QOvW4LMKxUGW4V8EBKXPOl+Oq3yfr4LZpG6P611cM9XGU4HazoF12tEUfbRaKF2DR4x0Vq9V+BVMIc8lKXI5lNEY5pL4MmoGApzv9o4A9AoGAQ2RdbX7CyukCRiHs/CKXKf02rytZtiSjNfzQz7FcBpjxG25XhWYiFJ+sHzJAhF27FACvk8Vy+ScIjUwBxbeHA0DRbHLad+TEBqexVQxo+0e0OdiCzmyYaCeo6BZC4ooHtFCvhDUg02fwmwh9lwmpea1v8RjMHSfemU8uVnXmubECgYEA1DXc47zIPMtk1OOLk5dzaWjFIwFJdcEmDckY4n3S+bmpcP30UPmUQ+RvZkrVJSMC/+fgCBND1ilCdKf8PNtzJdWHw5U0apFj82yyDJZsJBH4/6/EHeOiSBgUKXa3oTmFKRorUdMtRKBMicUNt6A7NHDvMVIbdIcoNPo9PSynrrE=";
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

}
