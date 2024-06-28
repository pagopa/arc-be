package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;
import it.gov.pagopa.arc.model.generated.InfoTransactionDTO;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class PaymentMethodEnumMapper {
    private PaymentMethodEnumMapper(){}
    protected static final Map<PaymentMethod, InfoTransactionDTO.PaymentMethodEnum> paymentMethodEnumMap;

    static {
        paymentMethodEnumMap = new EnumMap<>(PaymentMethod.class);
        paymentMethodEnumMap.put(PaymentMethod.BBT, InfoTransactionDTO.PaymentMethodEnum.BBT);
        paymentMethodEnumMap.put(PaymentMethod.BP, InfoTransactionDTO.PaymentMethodEnum.BP);
        paymentMethodEnumMap.put(PaymentMethod.AD, InfoTransactionDTO.PaymentMethodEnum.AD);
        paymentMethodEnumMap.put(PaymentMethod.CP, InfoTransactionDTO.PaymentMethodEnum.CP);
        paymentMethodEnumMap.put(PaymentMethod.PO, InfoTransactionDTO.PaymentMethodEnum.PO);
        paymentMethodEnumMap.put(PaymentMethod.OBEP, InfoTransactionDTO.PaymentMethodEnum.OBEP);
        paymentMethodEnumMap.put(PaymentMethod.JIF, InfoTransactionDTO.PaymentMethodEnum.JIF);
        paymentMethodEnumMap.put(PaymentMethod.MYBK, InfoTransactionDTO.PaymentMethodEnum.MYBK);
        paymentMethodEnumMap.put(PaymentMethod.PPAL, InfoTransactionDTO.PaymentMethodEnum.PPAL);
        paymentMethodEnumMap.put(PaymentMethod.UNKNOWN, InfoTransactionDTO.PaymentMethodEnum.UNKNOWN);
    }
}
