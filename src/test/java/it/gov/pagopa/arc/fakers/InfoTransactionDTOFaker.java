package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.InfoTransactionDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;

import java.time.ZonedDateTime;

public class InfoTransactionDTOFaker {

    public static InfoTransactionDTO mockInstance(WalletInfoDTO walletInfo, UserDetailDTO payer){
        return mockInstanceBuilder(walletInfo, payer).build();
    }
    private static InfoTransactionDTO.InfoTransactionDTOBuilder mockInstanceBuilder(WalletInfoDTO walletInfo, UserDetailDTO payer){
        return InfoTransactionDTO.builder()
                .transactionId("TRANSACTION_ID")
                .authCode("250863")
                .rrn("51561651")
                .transactionDate(ZonedDateTime.parse("2024-06-27T13:07:25Z"))
                .pspName("Worldline Merchant Services Italia S.p.A.")
                .walletInfo(walletInfo)
                .paymentMethod(InfoTransactionDTO.PaymentMethodEnum.PO)
                .payer(payer)
                .amount(565430L)
                .fee(29L)
                .origin(InfoTransactionDTO.OriginEnum.UNKNOWN);
    }
}
