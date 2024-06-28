package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.utils.Utilities;
import org.springframework.stereotype.Service;

@Service
public class BizEventsCartItem2CartItemDTO {
    private final BizEventsUserDetail2UserDetailDTO userDetailsMapper;

    public BizEventsCartItem2CartItemDTO(BizEventsUserDetail2UserDetailDTO userDetailsMapper) {
        this.userDetailsMapper = userDetailsMapper;
    }

    public CartItemDTO mapCart(BizEventsCartItemDTO bizEventsCartItemDTO){
        return CartItemDTO.builder()
                .subject(bizEventsCartItemDTO.getSubject())
                .amount(bizEventsCartItemDTO.getAmount() != null ? Utilities.euroToCents(bizEventsCartItemDTO.getAmount()) : null)
                .payee(userDetailsMapper.mapUserDetail(bizEventsCartItemDTO.getPayee()))
                .debtor(userDetailsMapper.mapUserDetail(bizEventsCartItemDTO.getDebtor()))
                .refNumberType(bizEventsCartItemDTO.getRefNumberType())
                .refNumberValue(bizEventsCartItemDTO.getRefNumberValue())
                .build();
    }
}
