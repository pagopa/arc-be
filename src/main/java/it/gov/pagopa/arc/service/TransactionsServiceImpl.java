package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionsServiceImpl implements TransactionsService {
    private final BizEventsService bizEventsService;

    public TransactionsServiceImpl(BizEventsService bizEventsService) {
        this.bizEventsService = bizEventsService;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsList(Integer page, Integer size, String filter) {
        return bizEventsService.retrieveTransactionsListFromBizEvents(page,size,filter);
    }
}

