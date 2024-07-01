package it.gov.pagopa.arc.fakers.bizEvents;

import it.gov.pagopa.arc.connector.bizevents.dto.*;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;

import java.util.List;

public class BizEventsTransactionDetailsDTOFaker {
    public static BizEventsTransactionDetailsDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static BizEventsTransactionDetailsDTO.BizEventsTransactionDetailsDTOBuilder mockInstanceBuilder(){
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        BizEventsUserDetailDTO payer = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        BizEventsInfoTransactionDTO bizEventsInfoTransaction = BizEventsInfoTransactionDTOFaker.mockInstance(bizEventsWalletInfo, payer);

        BizEventsUserDetailDTO payee = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        BizEventsUserDetailDTO debtor = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);
        BizEventsCartItemDTO bizEventsCartItem1 = BizEventsCartItemDTOFaker.mockInstance(payee,debtor);
        BizEventsCartItemDTO bizEventsCartItem2 = BizEventsCartItemDTOFaker.mockInstance(payee,debtor);
        List<BizEventsCartItemDTO> bizEventsCartsList = List.of(bizEventsCartItem1, bizEventsCartItem2);

        return BizEventsTransactionDetailsDTO.builder()
                .bizEventsInfoTransactionDTO(bizEventsInfoTransaction)
                .bizEventsCartsDTO(bizEventsCartsList);
    }
}
