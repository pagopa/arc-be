package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcNoticesApi;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.service.NoticesService;
import org.apache.commons.lang3.StringUtils;
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

        String continuationToken;
        if (StringUtils.isNotBlank(noticesListResponseDTO.getContinuationToken()))
            continuationToken = noticesListResponseDTO.getContinuationToken();
        else continuationToken = null;


        return ResponseEntity
                .ok()
                .header("x-continuation-token", continuationToken)
                .body(noticesListResponseDTO.getNoticesListDTO());
    }
}
