package it.gov.pagopa.arc.service;

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
    public NoticesListResponseDTO retrieveNoticesAndToken(String continuationToken, Integer size, Boolean paidByMe, Boolean registeredToMe, String orderBy, String ordering) {
        log.info("[GET_NOTICES_LIST] The current user has requested to retrieve his list of paid notices, with the current parameters: size {}, paidByMe {}, registeredToMe {}, orderBy {} and ordering {}",
                size, paidByMe, registeredToMe, orderBy, ordering);

        return bizEventsService.retrievePaidListFromBizEvents(continuationToken, size, paidByMe, registeredToMe, orderBy, ordering);
    }
}
