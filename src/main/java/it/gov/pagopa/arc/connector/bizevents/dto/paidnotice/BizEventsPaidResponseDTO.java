package it.gov.pagopa.arc.connector.bizevents.dto.paidnotice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class BizEventsPaidResponseDTO extends BizEventsPaidNoticeListDTO{
    private String continuationToken;
}
