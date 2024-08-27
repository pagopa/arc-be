package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.*;

import java.util.List;

public class TransactionDetailsDTOFaker {
    public static TransactionDetailsDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static TransactionDetailsDTO.TransactionDetailsDTOBuilder mockInstanceBuilder(){
        WalletInfoDTO walletInfo = CommonWalletInfoDTOFaker.mockWalletInfoDTO(false);
        UserDetailDTO payerMapped = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        InfoTransactionDTO infoTransaction = InfoTransactionDTOFaker.mockInstance(walletInfo, payerMapped);

        UserDetailDTO payeeResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        UserDetailDTO debtorResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);
        CartItemDTO cartItem1 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        CartItemDTO cartItem2 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        List<CartItemDTO> cartsList = List.of(cartItem1, cartItem2);

        return TransactionDetailsDTO.builder()
                .infoTransaction(infoTransaction)
                .carts(cartsList);
    }

}
