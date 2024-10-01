package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class BizEventsUserDetail2UserDetailDTOMapper {

    public UserDetailDTO mapUserDetail(BizEventsUserDetailDTO bizEventsUserDetailDTO){
        return UserDetailDTO.builder()
                .name(bizEventsUserDetailDTO.getName())
                .taxCode(bizEventsUserDetailDTO.getTaxCode())
                .build();
    }
}
