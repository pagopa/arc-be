package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;

import java.lang.reflect.Method;

public class CommonWalletInfoDTOFaker {

    public static WalletInfoDTO mockWalletInfoDTO(boolean paypal) {
        return (WalletInfoDTO) buildWalletInfoDTO(WalletInfoDTO.builder(), paypal);
    }

    public static BizEventsWalletInfoDTO mockBizEventsWalletInfoDTO(boolean paypal) {
        return (BizEventsWalletInfoDTO) buildWalletInfoDTO(BizEventsWalletInfoDTO.builder(), paypal);
    }

    private static Object buildWalletInfoDTO(Object builder, boolean paypal) {
        try {
            Method accountHolderMethod = builder.getClass().getMethod("accountHolder", String.class);
            Method brandMethod = builder.getClass().getMethod("brand", String.class);
            Method blurredNumberMethod = builder.getClass().getMethod("blurredNumber", String.class);
            Method maskedEmailMethod = builder.getClass().getMethod("maskedEmail", String.class);
            Method buildMethod = builder.getClass().getMethod("build");

            accountHolderMethod.invoke(builder, "USER_HOLDER");
            brandMethod.invoke(builder, "VISA");
            blurredNumberMethod.invoke(builder, "0932");

            if (paypal) {
                maskedEmailMethod.invoke(builder, "user@paypal.com");
            }

            return buildMethod.invoke(builder);

        } catch (Exception e) {
            throw new RuntimeException("Error building DTO", e);
        }
    }

}
