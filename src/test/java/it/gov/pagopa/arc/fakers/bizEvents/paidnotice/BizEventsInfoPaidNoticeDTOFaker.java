package it.gov.pagopa.arc.fakers.bizEvents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsInfoPaidNoticeDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;
import it.gov.pagopa.arc.utils.TestUtils;

public class BizEventsInfoPaidNoticeDTOFaker {
    public static BizEventsInfoPaidNoticeDTO mockInstance(BizEventsWalletInfoDTO walletInfo, BizEventsUserDetailDTO payer){
        return mockInstanceBuilder(walletInfo, payer).build();
    }
    private static BizEventsInfoPaidNoticeDTO.BizEventsInfoPaidNoticeDTOBuilder mockInstanceBuilder(BizEventsWalletInfoDTO walletInfo, BizEventsUserDetailDTO payer){
        BizEventsInfoPaidNoticeDTO.BizEventsInfoPaidNoticeDTOBuilder bizEventsInfoPaidNoticeDTOBuilder = BizEventsInfoPaidNoticeDTO.builder()
                .eventId("EVENT_ID")
                .authCode("250863")
                .rrn("51561651")
                .noticeDate("2024-06-27T13:07:25Z")
                .pspName("Worldline Merchant Services Italia S.p.A.")
                .walletInfo(walletInfo)
                .paymentMethod(PaymentMethod.PO)
                .payer(payer)
                .amount("5,654.3")
                .fee("0.29")
                .origin(Origin.PM);

        TestUtils.assertNotNullFields(bizEventsInfoPaidNoticeDTOBuilder);
        return bizEventsInfoPaidNoticeDTOBuilder;
    }
}
