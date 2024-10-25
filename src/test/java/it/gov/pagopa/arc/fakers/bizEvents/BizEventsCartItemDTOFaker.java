package it.gov.pagopa.arc.fakers.bizEvents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;

public class BizEventsCartItemDTOFaker {

    public static BizEventsCartItemDTO mockInstance(BizEventsUserDetailDTO payee, BizEventsUserDetailDTO debtor){
        return mockInstanceBuilder(payee, debtor).build();
    }
    private static BizEventsCartItemDTO.BizEventsCartItemDTOBuilder mockInstanceBuilder(BizEventsUserDetailDTO payee, BizEventsUserDetailDTO debtor){
        return BizEventsCartItemDTO.builder()
                .subject("pagamento")
                .amount("5,452.3")
                .payee(payee)
                .debtor(debtor)
                .refNumberValue("960000000094659945")
                .refNumberType("IUV");
    }
}
