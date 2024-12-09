package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MapperUtilities.class, GPDPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapper.class})
public interface GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper {
    @Mapping(target = "paTaxCode", source = "organizationFiscalCode")
    @Mapping(target = "paFullName", source = "companyName")
    @Mapping(target = "paymentOptions", source = "paymentOption")
    PaymentNoticeDetailsDTO toPaymentNoticeDetailsDTO(GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTO);
}
