package it.gov.pagopa.arc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JsonConfig;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestUtils {
    private TestUtils(){}
    /**
     * application's objectMapper
     */
    public static final ObjectMapper objectMapper = new JsonConfig().objectMapper();

    public static void wait(long timeout, TimeUnit timeoutUnit) {
        try{
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .timeout(timeout, timeoutUnit)
                    .pollInterval(timeout, timeoutUnit)
                    .until(()->false);
        } catch (ConditionTimeoutException ex){
            // Do Nothing
        }
    }

    public static String genToken(RSAPublicKey publicKey, RSAPrivateKey privateKey,int ttl,String issuer){
        return JWT.create()
            .withClaim("typ","Bearer" )
            .withClaim("sub","_7284fdec21b65e716223feeb9b3564c1")
            .withClaim("familyName","Polo")
            .withClaim("name","Marco")
            .withClaim("fiscalNumber","TINIT-PLOMRC01P30L736Y")
            .withClaim("email","ilmilione@virgilio.it")
            .withClaim("aud","mockAudience")
            .withClaim("nonce","nonce")
            .withIssuer(issuer)
            .withJWTId(UUID.randomUUID().toString())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(ttl))
            .sign(Algorithm.RSA512(publicKey, privateKey));
    }

    public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // You can choose the key size
        return keyGen.generateKeyPair();
    }

    public static void assertNotNullFields(Object o, String... excludedFields) {
        Set<String> excludedFieldsSet = new HashSet<>(Arrays.asList(excludedFields));

        org.springframework.util.ReflectionUtils.doWithFields(o.getClass(),
                f -> {
                    f.setAccessible(true);
                    Assertions.assertNotNull(f.get(o), "The field %s of the input object of type %s is null!".formatted(f.getName(), o.getClass()));
                },
                f -> !excludedFieldsSet.contains(f.getName()));
    }



}
