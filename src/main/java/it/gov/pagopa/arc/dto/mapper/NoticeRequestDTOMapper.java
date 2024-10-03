package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class NoticeRequestDTOMapper {
    public NoticeRequestDTO apply(String continuationToken, Integer size, Boolean paidByMe, Boolean registeredToMe, String orderBy, String ordering){
        return NoticeRequestDTO.builder()
                .continuationToken(continuationToken)
                .size(size)
                .paidByMe(paidByMe)
                .registeredToMe(registeredToMe)
                .orderBy(orderBy)
                .ordering(ordering)
                .build();
    }
}
