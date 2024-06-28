package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.model.generated.InfoTransactionDTO;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class OriginEnumMapper {
    private OriginEnumMapper(){}
    protected static final Map<Origin, InfoTransactionDTO.OriginEnum> originEnumMap;

    static {
        originEnumMap = new EnumMap<>(Origin.class);
        originEnumMap.put(Origin.INTERNAL, InfoTransactionDTO.OriginEnum.INTERNAL);
        originEnumMap.put(Origin.PM, InfoTransactionDTO.OriginEnum.PM);
        originEnumMap.put(Origin.NDP001PROD, InfoTransactionDTO.OriginEnum.NDP001PROD);
        originEnumMap.put(Origin.NDP002PROD, InfoTransactionDTO.OriginEnum.NDP002PROD);
        originEnumMap.put(Origin.NDP003PROD, InfoTransactionDTO.OriginEnum.NDP003PROD);
        originEnumMap.put(Origin.UNKNOWN, InfoTransactionDTO.OriginEnum.UNKNOWN);
    }
}
