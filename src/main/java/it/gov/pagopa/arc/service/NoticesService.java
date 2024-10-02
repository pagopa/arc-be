package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;

public interface NoticesService {
    NoticesListResponseDTO retrieveNoticesAndToken(String userFiscalCode, String userId, NoticeRequestDTO noticeRequestDTO);
}
