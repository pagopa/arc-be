package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcNoticesApi;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.service.NoticesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesControllerImpl implements ArcNoticesApi {
    private final NoticesService noticesService;

    public NoticesControllerImpl(NoticesService noticesService) {
        this.noticesService = noticesService;
    }

    @Override
    public ResponseEntity<NoticesListDTO> getNoticesList(String xContinuationToken, Integer size, Boolean paidByMe, Boolean registeredToMe, String orderBy, String ordering) {
        NoticesListResponseDTO noticesListResponseDTO = noticesService.retrieveNoticesAndToken(xContinuationToken, size, paidByMe, registeredToMe, orderBy, ordering);

        String continuationToken = StringUtils.isNotBlank(noticesListResponseDTO.getContinuationToken()) ? noticesListResponseDTO.getContinuationToken() : null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-continuation-token", continuationToken );

        return new ResponseEntity<>(noticesListResponseDTO.getNoticesListDTO(), headers, HttpStatus.OK);
    }
}
