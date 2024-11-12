package it.gov.pagopa.arc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRequestDTO {
    private String continuationToken;
    private Integer size;
    private Boolean paidByMe;
    private Boolean registeredToMe;
    private String orderBy;
    private String ordering;

}
