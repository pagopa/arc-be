package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.dto.NoticesListResponseDTO;

public interface NoticesService {
    NoticesListResponseDTO retrieveNoticesAndToken(String continuationToken, Integer size, Boolean paidByMe, Boolean registeredToMe, String orderBy, String ordering);
}
