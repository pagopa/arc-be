package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.utils.Utilities;
import org.springframework.stereotype.Component;

@Component
public class BizEventsCartItem2CartItemDTOMapper {
    private final BizEventsUserDetail2UserDetailDTOMapper userDetailsMapper;

    public BizEventsCartItem2CartItemDTOMapper(BizEventsUserDetail2UserDetailDTOMapper userDetailsMapper) {
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
