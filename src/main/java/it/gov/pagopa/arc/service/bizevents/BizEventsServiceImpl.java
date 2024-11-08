package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnector;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class BizEventsServiceImpl implements BizEventsService{

    private final BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector;
    private final BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;

    public BizEventsServiceImpl(BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector,
                                BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper) {
        this.bizEventsPaidNoticeConnector = bizEventsPaidNoticeConnector;
        this.bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper = bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;
    }

    @Override
    public NoticesListResponseDTO retrievePaidListFromBizEvents(String userFiscalCode, NoticeRequestDTO noticeRequestDTO) {
         return bizEventsPaidNoticeConnector.getPaidNoticeList(userFiscalCode, noticeRequestDTO);
    }

    @Override
    public NoticeDetailsDTO retrievePaidNoticeDetailsFromBizEvents(String userId, String userFiscalCode, String eventId) {
        BizEventsPaidNoticeDetailsDTO paidNoticeDetails = bizEventsPaidNoticeConnector.getPaidNoticeDetails(userId, userFiscalCode, eventId);
        return bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper.toNoticeDetailsDTO(paidNoticeDetails);
    }

    @Override
    public Resource retrievePaidNoticeReceiptFromBizEvents(String userId, String userFiscalCode, String eventId) {
        return bizEventsPaidNoticeConnector.getPaidNoticeReceipt(userId, userFiscalCode, eventId);
    }
}
