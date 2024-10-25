package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {MapperUtilities.class, BizEventsUserDetail2UserDetailDTOMapper.class})
public interface BizEventsCartItem2CartItemDTOMapper {

    @Mapping(source = "amount", target = "amount", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "euroToCents")
    CartItemDTO mapCart(BizEventsCartItemDTO bizEventsCartItemDTO);

}
