package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcNoticesApi;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.NoticeRequestDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.service.NoticesService;
import it.gov.pagopa.arc.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesControllerImpl implements ArcNoticesApi {
    private final NoticesService noticesService;
    private final NoticeRequestDTOMapper noticeRequestDTOMapper;

    public NoticesControllerImpl(NoticesService noticesService, NoticeRequestDTOMapper noticeRequestDTOMapper) {
        this.noticesService = noticesService;
        this.noticeRequestDTOMapper = noticeRequestDTOMapper;
    }

    @Override
    public ResponseEntity<NoticesListDTO> getNoticesList(String xContinuationToken, Integer size, Boolean paidByMe, Boolean registeredToMe, String orderBy, String ordering) {
        String userFiscalCode = SecurityUtils.getUserFiscalCode();
        String userId = SecurityUtils.getUserId();

        NoticeRequestDTO requestDTO = noticeRequestDTOMapper.apply(xContinuationToken, size, paidByMe, registeredToMe, orderBy, ordering);

        NoticesListResponseDTO noticesListResponseDTO = noticesService.retrieveNoticesAndToken(userFiscalCode, userId, requestDTO);

        String continuationToken = null;
        if (StringUtils.isNotBlank(noticesListResponseDTO.getContinuationToken())){
            continuationToken = noticesListResponseDTO.getContinuationToken();
        }


        return ResponseEntity
                .ok()
                .header("x-continuation-token", continuationToken)
                .body(noticesListResponseDTO.getNoticesListDTO());
    }

    @Override
    public ResponseEntity<NoticeDetailsDTO> getNoticeDetails(String eventId) {
        String userFiscalCode = SecurityUtils.getUserFiscalCode();
        String userId = SecurityUtils.getUserId();
        NoticeDetailsDTO noticeDetailsDTO = noticesService.retrieveNoticeDetails(userId, userFiscalCode, eventId);

        return ResponseEntity.ok().body(noticeDetailsDTO);
    }

    @Override
    public ResponseEntity<Resource> getNoticeReceipt(String eventId) {
        String userFiscalCode = SecurityUtils.getUserFiscalCode();
        String userId = SecurityUtils.getUserId();

        Resource receipt = noticesService.retrieveNoticeReceipt(userId, userFiscalCode, eventId);

        return ResponseEntity.ok().body(receipt);
    }
}
