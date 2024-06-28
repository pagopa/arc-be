package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;

import java.lang.reflect.Method;

public class CommonUserDetailDTOFaker {
    public static final String USER_DETAIL_PAYER = "payer";
    public static final String USER_DETAIL_DEBTOR = "debtor";
    public static final String USER_DETAIL_PAYEE = "payee";

    public static UserDetailDTO mockUserDetailDTO(String role) {
        return (UserDetailDTO) buildUserDetailDTO(UserDetailDTO.builder(), role);
    }
    public static BizEventsUserDetailDTO mockBizEventsUserDetailDTO(String role) {
        return (BizEventsUserDetailDTO) buildUserDetailDTO(BizEventsUserDetailDTO.builder(), role);
    }

    private static Object buildUserDetailDTO(Object builder, String role) {
        try {
            Method nameMethod = builder.getClass().getMethod("name", String.class);
            Method taxCodeMethod = builder.getClass().getMethod("taxCode", String.class);
            Method buildMethod = builder.getClass().getMethod("build");

            switch (role) {
                case "payee" -> {
                    nameMethod.invoke(builder, "CREDITOR_NAME");
                    taxCodeMethod.invoke(builder, "CREDITOR_TAX_CODE");
                }
                case "debtor" -> {
                    nameMethod.invoke(builder, "USER_DEBTOR");
                    taxCodeMethod.invoke(builder, "FAKE_CF_DEBTOR");
                }
                case "payer" -> {
                    nameMethod.invoke(builder, "USER_PAYER");
                    taxCodeMethod.invoke(builder, "FAKE_CF_PAYER");
                }
                default -> throw new IllegalArgumentException("Unsupported role: " + role);
            }

            return buildMethod.invoke(builder);

        } catch (Exception e) {
            throw new RuntimeException("Error building DTO", e);
        }
    }
}
