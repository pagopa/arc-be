package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsInfoTransactionDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;
import it.gov.pagopa.arc.model.generated.InfoTransactionDTO;
import it.gov.pagopa.arc.utils.Utilities;
import org.springframework.stereotype.Component;

@Component
public class BizEventsInfoTransaction2InfoTransactionDTOMapper {
    private final BizEventsWalletInfo2WalletInfoDTOMapper walletInfoMapper;
    private final BizEventsUserDetail2UserDetailDTOMapper userDetailsMapper;
    public BizEventsInfoTransaction2InfoTransactionDTOMapper(BizEventsWalletInfo2WalletInfoDTOMapper walletInfoMapper, BizEventsUserDetail2UserDetailDTOMapper userDetailsMapper) {
        this.walletInfoMapper = walletInfoMapper;
        this.userDetailsMapper = userDetailsMapper;
    }

    public InfoTransactionDTO mapInfoTransaction(BizEventsInfoTransactionDTO bizEventsInfoTransactionDTO){
        PaymentMethod paymentMethod = null;
        if(bizEventsInfoTransactionDTO.getPaymentMethod() != null) {
            paymentMethod = bizEventsInfoTransactionDTO.getPaymentMethod();
        }
        return InfoTransactionDTO.builder()
                .transactionId(bizEventsInfoTransactionDTO.getTransactionId())
                .authCode(bizEventsInfoTransactionDTO.getAuthCode())
                .rrn(bizEventsInfoTransactionDTO.getRrn())
                .transactionDate(bizEventsInfoTransactionDTO.getTransactionDate() != null ? Utilities.dateStringToZonedDateTime(bizEventsInfoTransactionDTO.getTransactionDate()) : null)
                .pspName(bizEventsInfoTransactionDTO.getPspName())
                .walletInfo(bizEventsInfoTransactionDTO.getBizEventsWalletInfoDTO() != null ? walletInfoMapper.mapWalletInfo(bizEventsInfoTransactionDTO.getBizEventsWalletInfoDTO()) : null )
                .paymentMethod(paymentMethod != null ? PaymentMethodEnumMapper.paymentMethodEnumMap.get(paymentMethod) : null)
                .payer(bizEventsInfoTransactionDTO.getPayer() != null ? userDetailsMapper.mapUserDetail(bizEventsInfoTransactionDTO.getPayer()) : null)
                .amount(bizEventsInfoTransactionDTO.getAmount() != null ? Utilities.euroToCents(bizEventsInfoTransactionDTO.getAmount()) : null)
                .fee(bizEventsInfoTransactionDTO.getFee() != null ? Utilities.euroToCents(bizEventsInfoTransactionDTO.getFee()) : null)
                .origin(OriginEnumMapper.originEnumMap.get(bizEventsInfoTransactionDTO.getOrigin()))
                .build();
    }

}
