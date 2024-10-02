package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoticesServiceImpl implements NoticesService{
    private final BizEventsService bizEventsService;

    public NoticesServiceImpl(BizEventsService bizEventsService) {
        this.bizEventsService = bizEventsService;
    }

    @Override
    public NoticesListResponseDTO retrieveNoticesAndToken(String userFiscalCode, String userId, NoticeRequestDTO noticeRequestDTO) {

        log.info("[GET_NOTICES_LIST] The current user with id : {}, has requested to retrieve his list of paid notices, with the current parameters: size {}, paidByMe {}, registeredToMe {}, orderBy {} and ordering {}",
                userId, noticeRequestDTO.getSize(), noticeRequestDTO.getPaidByMe(),  noticeRequestDTO.getRegisteredToMe(),  noticeRequestDTO.getOrderBy(),  noticeRequestDTO.getOrdering());

        return bizEventsService.retrievePaidListFromBizEvents(userFiscalCode, noticeRequestDTO);
    }
}
