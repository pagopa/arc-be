package it.gov.pagopa.arc.fakers.bizEvents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsInfoTransactionDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;

public class BizEventsInfoTransactionDTOFaker {
    public static BizEventsInfoTransactionDTO mockInstance(BizEventsWalletInfoDTO walletInfo, BizEventsUserDetailDTO payer){
        return mockInstanceBuilder(walletInfo, payer).build();
    }
    private static BizEventsInfoTransactionDTO.BizEventsInfoTransactionDTOBuilder mockInstanceBuilder(BizEventsWalletInfoDTO walletInfo, BizEventsUserDetailDTO payer){
        return BizEventsInfoTransactionDTO.builder()
            .transactionId("TRANSACTION_ID")
            .authCode("250863")
            .rrn("51561651")
            .transactionDate("2024-06-27T13:07:25Z")
            .pspName("Worldline Merchant Services Italia S.p.A.")
            .bizEventsWalletInfoDTO(walletInfo)
            .paymentMethod(PaymentMethod.PO)
            .payer(payer)
            .amount("5,654.3")
            .fee("0.29")
            .origin(Origin.PM);
    }
}
