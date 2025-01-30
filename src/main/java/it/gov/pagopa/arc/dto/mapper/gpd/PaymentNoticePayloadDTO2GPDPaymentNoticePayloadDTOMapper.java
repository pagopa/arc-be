package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper {
    public GPDPaymentNoticePayloadDTO map(IamUserInfoDTO iamUserInfoDTO, PaymentNoticePayloadDTO paymentNoticePayloadDTO){

        if(paymentNoticePayloadDTO != null && iamUserInfoDTO != null){
            //generate iupd
            String iupd = "ARp-".concat(UUID.randomUUID().toString());

            //concat name and familyName
            String name = iamUserInfoDTO.getName();
            String familyName = iamUserInfoDTO.getFamilyName();
            String fullName = name.concat(" ").concat(familyName);


            return GPDPaymentNoticePayloadDTO.builder()
                    .iupd(iupd)
                    .type(GPDDebtorType.F)
                    .fiscalCode(iamUserInfoDTO.getFiscalCode())
                    .fullName(fullName)
                    .switchToExpired(true)
                    .companyName(paymentNoticePayloadDTO.getPaFullName())
                    .paymentOption(mapToGPDPaymentOptionPayloadDTO(paymentNoticePayloadDTO))
                    .build();

        }else {
            return null;
        }
    }


    private List<GPDPaymentOptionPayloadDTO> mapToGPDPaymentOptionPayloadDTO(PaymentNoticePayloadDTO paymentNoticePayloadDTO){
        //generate iuv
        String iuv = "02".concat(String.valueOf(System.currentTimeMillis()));

        //dueDate
        Instant dueDateInstant = Instant.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dueDate = LocalDateTime.ofInstant(dueDateInstant, ZoneId.systemDefault());

        return Collections.singletonList(
                GPDPaymentOptionPayloadDTO.builder()
                .iuv(iuv)
                .amount(paymentNoticePayloadDTO.getAmount())
                .description(paymentNoticePayloadDTO.getDescription())
                .isPartialPayment(false)
                .dueDate(dueDate)
                .transfer(mapToGPDTransferPayloadDTO(paymentNoticePayloadDTO))
                .build());
    }

    private List<GPDTransferPayloadDTO> mapToGPDTransferPayloadDTO(PaymentNoticePayloadDTO paymentNoticePayloadDTO){
        return Collections.singletonList(
                GPDTransferPayloadDTO.builder()
                .idTransfer("1")
                .amount(paymentNoticePayloadDTO.getAmount())
                .remittanceInformation(paymentNoticePayloadDTO.getDescription())
                .category("9/0101108TS/")
                .iban("IT39X0300203280451585346538")
                .build());
    }
}
