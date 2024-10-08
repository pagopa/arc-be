package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.model.generated.*;
import it.gov.pagopa.arc.utils.TestUtils;

import java.util.List;

public class NoticeDetailsDTOFaker {
    public static NoticeDetailsDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static NoticeDetailsDTO.NoticeDetailsDTOBuilder mockInstanceBuilder(){
        WalletInfoDTO walletInfo = CommonWalletInfoDTOFaker.mockWalletInfoDTO(false);
        UserDetailDTO payerMapped = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        InfoNoticeDTO infoNotice = InfoNoticeDTOFaker.mockInstance(walletInfo, payerMapped);

        UserDetailDTO payeeResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        UserDetailDTO debtorResponse = CommonUserDetailDTOFaker.mockUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);
        CartItemDTO cartItem1 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        CartItemDTO cartItem2 = CartItemDTOFaker.mockInstance(payeeResponse, debtorResponse);
        List<CartItemDTO> cartsList = List.of(cartItem1, cartItem2);

        NoticeDetailsDTO.NoticeDetailsDTOBuilder noticeDetailsDTOBuilder = NoticeDetailsDTO.builder()
                .infoNotice(infoNotice)
                .carts(cartsList);
        TestUtils.assertNotNullFields(noticeDetailsDTOBuilder);

        return noticeDetailsDTOBuilder;
    }
}
