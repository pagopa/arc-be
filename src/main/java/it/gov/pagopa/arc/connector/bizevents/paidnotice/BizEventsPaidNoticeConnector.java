package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;

public interface BizEventsPaidNoticeConnector {
    BizEventsPaidNoticeListDTO getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering);
}
