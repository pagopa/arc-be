package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BizEventsTransactionDetails2TransactionDetailsDTOMapper {
    private final BizEventsInfoTransaction2InfoTransactionDTOMapper transactionInfoMapper;
    private final BizEventsCartItem2CartItemDTOMapper cartItemDTOMapper;

    public BizEventsTransactionDetails2TransactionDetailsDTOMapper(BizEventsInfoTransaction2InfoTransactionDTOMapper transactionInfoMapper, BizEventsCartItem2CartItemDTOMapper cartItemDTOMapper) {
        this.transactionInfoMapper = transactionInfoMapper;
        this.cartItemDTOMapper = cartItemDTOMapper;
    }

    public TransactionDetailsDTO apply(BizEventsTransactionDetailsDTO bizEventsTransactionDetailsDTO){
        List<CartItemDTO> carts = bizEventsTransactionDetailsDTO
                .getBizEventsCartsDTO()
                .stream()
                .map(cartItemDTOMapper::mapCart)
                .toList();

        return TransactionDetailsDTO.builder()
                .infoTransaction(transactionInfoMapper.mapInfoTransaction(bizEventsTransactionDetailsDTO.getBizEventsInfoTransactionDTO()))
                .carts(carts)
                .build();
    }
}
