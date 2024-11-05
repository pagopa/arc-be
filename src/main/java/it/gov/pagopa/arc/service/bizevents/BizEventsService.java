package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.core.io.Resource;

public interface BizEventsService {
    TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter);
    NoticesListResponseDTO retrievePaidListFromBizEvents(String userFiscalCode, NoticeRequestDTO noticeRequestDTO);
    NoticeDetailsDTO retrievePaidNoticeDetailsFromBizEvents(String userId, String userFiscalCode, String eventId);
    Resource retrievePaidNoticeReceiptFromBizEvents(String userId, String userFiscalCode, String eventId);
}
