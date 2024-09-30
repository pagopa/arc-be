package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidResponseDTO;

public interface BizEventsPaidNoticeConnector {
    BizEventsPaidResponseDTO getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering);
}
