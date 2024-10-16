package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import org.springframework.core.io.Resource;

public interface BizEventsPaidNoticeConnector {
    NoticesListResponseDTO getPaidNoticeList(String fiscalCode, NoticeRequestDTO noticeRequestDTO);
    BizEventsPaidNoticeDetailsDTO getPaidNoticeDetails(String userId, String userFiscalCode, String eventId);
    Resource getPaidNoticeReceipt(String userId, String userFiscalCode, String eventId);
}
