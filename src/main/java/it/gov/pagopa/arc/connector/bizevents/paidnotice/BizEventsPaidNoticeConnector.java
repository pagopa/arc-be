package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import feign.Response;

public interface BizEventsPaidNoticeConnector {
    Response getPaidNoticeList(String fiscalCode, String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering);
}
