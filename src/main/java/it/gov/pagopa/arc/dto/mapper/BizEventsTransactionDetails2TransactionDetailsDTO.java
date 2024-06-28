package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizEventsTransactionDetails2TransactionDetailsDTO {
    private final BizEventsTransactionInfo2TransactionInfoDTO transactionInfoMapper;

    public BizEventsTransactionDetails2TransactionDetailsDTO(BizEventsTransactionInfo2TransactionInfoDTO transactionInfoMapper) {
        this.transactionInfoMapper = transactionInfoMapper;
    }

    public TransactionDetailsDTO apply(BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO, List<CartItemDTO> cartsList){
        return TransactionDetailsDTO.builder()
                .infoTransaction(transactionInfoMapper.mapInfoTransaction(bizEventsTransactionDetailsDTO.getBizEventsInfoTransactionDTO()))
                .carts(cartsList)
                .build();
    }
}
