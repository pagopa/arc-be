package it.gov.pagopa.arc.fakers.bizEvents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsInfoPaidNoticeDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsCartItemDTOFaker;
import it.gov.pagopa.arc.utils.TestUtils;

import java.util.List;

public class BizEventsPaidNoticeDetailsDTOFaker {
    public static BizEventsPaidNoticeDetailsDTO mockInstance(){
        return mockInstanceBuilder().build();
    }

    public static BizEventsPaidNoticeDetailsDTO.BizEventsPaidNoticeDetailsDTOBuilder mockInstanceBuilder(){
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        BizEventsUserDetailDTO payer = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYER);
        BizEventsInfoPaidNoticeDTO bizEventsInfoPaidNoticeDTO = BizEventsInfoPaidNoticeDTOFaker.mockInstance(bizEventsWalletInfo, payer);

        BizEventsUserDetailDTO payeeResponse = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        BizEventsUserDetailDTO debtorResponse = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);

        BizEventsCartItemDTO bizEventsCartItem1 = BizEventsCartItemDTOFaker.mockInstance(payeeResponse,debtorResponse);
        BizEventsCartItemDTO bizEventsCartItem2 = BizEventsCartItemDTOFaker.mockInstance(payeeResponse,debtorResponse);
        List<BizEventsCartItemDTO> bizEventsCartsList = List.of(bizEventsCartItem1, bizEventsCartItem2);

        BizEventsPaidNoticeDetailsDTO.BizEventsPaidNoticeDetailsDTOBuilder bizEventsPaidNoticeDetailsDTO = BizEventsPaidNoticeDetailsDTO.builder()
                .infoNotice(bizEventsInfoPaidNoticeDTO)
                .carts(bizEventsCartsList);

        TestUtils.assertNotNullFields(bizEventsPaidNoticeDetailsDTO);

        return bizEventsPaidNoticeDetailsDTO;
    }
}
