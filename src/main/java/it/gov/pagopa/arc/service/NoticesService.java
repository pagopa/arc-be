package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;

public interface NoticesService {
    NoticesListResponseDTO retrieveNoticesAndToken(String userFiscalCode, String userId, NoticeRequestDTO noticeRequestDTO);
    NoticeDetailsDTO retrieveNoticeDetails(String userId, String userFiscalCode, String eventId);
}
