package it.gov.pagopa.arc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.gov.pagopa.arc.config.ZendeskAssistanceTokenConfig;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.utils.SecurityUtils;
import it.gov.pagopa.arc.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ZendeskAssistanceTokenBuilder {
    private final ZendeskAssistanceTokenConfig zendeskConfig;

    public ZendeskAssistanceTokenBuilder(ZendeskAssistanceTokenConfig zendeskConfig) {
        this.zendeskConfig = zendeskConfig;
    }

    public String buildZendeskAssistanceToken(String userEmail) {
        Map<String, String> imUserInfoMap = mapImUserInfo(userEmail);

        String nameFromEmailAssistanceToken = Utilities.extractNameFromEmailAssistanceToken(userEmail);

        Algorithm algorithm = Algorithm.HMAC256(zendeskConfig.getAssistanceToken().getPrivateKey());

        return JWT.create()
                .withHeader(Map.of("typ", zendeskConfig.getTokenType()))
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(Instant.now())
                .withClaim("name", nameFromEmailAssistanceToken)
                .withClaim("email", userEmail)
                .withClaim("organization", zendeskConfig.getAssistanceToken().getOrganization())
                .withClaim("product_id", zendeskConfig.getAssistanceToken().getProductId())
                .withClaim("user_fields", imUserInfoMap)
                .sign(algorithm);
    }

    private Map<String, String> mapImUserInfo(String userEmail){

        IamUserInfoDTO iamUserInfoDTO = SecurityUtils.getPrincipal();

        return Map.of(
                "aux_data", iamUserInfoDTO.getFiscalCode(),
                "name", iamUserInfoDTO.getName(),
                "familyName", iamUserInfoDTO.getFamilyName(),
                "email", userEmail
        );

    }
}
