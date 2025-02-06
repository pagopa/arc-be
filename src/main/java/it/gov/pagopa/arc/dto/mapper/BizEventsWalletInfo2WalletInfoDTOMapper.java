package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BizEventsWalletInfo2WalletInfoDTOMapper {
    @Mapping(target = "brand", source = "brand", qualifiedByName = "mapBrandEnum")
    WalletInfoDTO mapToWalletInfo(BizEventsWalletInfoDTO bizEventsWalletInfoDTO);

    @Named("mapBrandEnum")
    default WalletInfoDTO.BrandEnum mapBrandEnum(String brand) {
        try {
            return WalletInfoDTO.BrandEnum.valueOf(brand);
        } catch (RuntimeException e) {
            return WalletInfoDTO.BrandEnum.OTHER;
        }
    }
}
