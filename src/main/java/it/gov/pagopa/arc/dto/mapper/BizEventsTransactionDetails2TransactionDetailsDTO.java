package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizEventsTransactionDetails2TransactionDetailsDTO {
    private final BizEventsInfoTransaction2InfoTransactionDTO transactionInfoMapper;

    public BizEventsTransactionDetails2TransactionDetailsDTO(BizEventsInfoTransaction2InfoTransactionDTO transactionInfoMapper) {
        this.transactionInfoMapper = transactionInfoMapper;
    }

    public TransactionDetailsDTO apply(BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO, List<CartItemDTO> cartsList){
        return TransactionDetailsDTO.builder()
                .infoTransaction(transactionInfoMapper.mapInfoTransaction(bizEventsTransactionDetailsDTO.getBizEventsInfoTransactionDTO()))
                .carts(cartsList)
                .build();
    }
}
