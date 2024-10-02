package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidResponseDTO;
import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnector;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.NoticesListResponseDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDetails2TransactionDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTOMapper;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDTO2NoticeDTOMapper;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeListDTO2NoticesListDTOMapper;
import it.gov.pagopa.arc.model.generated.*;
import it.gov.pagopa.arc.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BizEventsServiceImpl implements BizEventsService{

    private final BizEventsConnector bizEventsConnector;
    private final BizEventsTransactionDTO2TransactionDTOMapper transactionDTOMapper;
    private final BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapper;
    private final BizEventsTransactionDetails2TransactionDetailsDTOMapper transactionDetailsDTOMapper;
    private final BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector;
    private final BizEventsPaidNoticeDTO2NoticeDTOMapper bizEventsPaidNoticeDTO2NoticeDTOMapper;
    private final BizEventsPaidNoticeListDTO2NoticesListDTOMapper bizEventsPaidNoticeListDTO2NoticesListDTOMapper;
    private final NoticesListResponseDTOMapper noticesListResponseDTOMapper;
    public BizEventsServiceImpl(BizEventsConnector bizEventsConnector,
                                BizEventsTransactionDTO2TransactionDTOMapper transactionDTOMapper,
                                BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapper,
                                BizEventsTransactionDetails2TransactionDetailsDTOMapper transactionDetailsDTOMapper,
                                BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector, BizEventsPaidNoticeDTO2NoticeDTOMapper bizEventsPaidNoticeDTO2NoticeDTOMapper,
                                BizEventsPaidNoticeListDTO2NoticesListDTOMapper bizEventsPaidNoticeListDTO2NoticesListDTOMapper, NoticesListResponseDTOMapper noticesListResponseDTOMapper) {
        this.bizEventsConnector = bizEventsConnector;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionsListDTOMapper = transactionsListDTOMapper;
        this.transactionDetailsDTOMapper = transactionDetailsDTOMapper;
        this.bizEventsPaidNoticeConnector = bizEventsPaidNoticeConnector;
        this.bizEventsPaidNoticeDTO2NoticeDTOMapper = bizEventsPaidNoticeDTO2NoticeDTOMapper;
        this.bizEventsPaidNoticeListDTO2NoticesListDTOMapper = bizEventsPaidNoticeListDTO2NoticesListDTOMapper;
        this.noticesListResponseDTOMapper = noticesListResponseDTOMapper;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter) {
        String retrievedUserFiscalCode = SecurityUtils.getUserFiscalCode();
        List<TransactionDTO> transactions;
        BizEventsTransactionsListDTO bizEventsTransactionsList = bizEventsConnector.getTransactionsList(retrievedUserFiscalCode, size);
        if(!bizEventsTransactionsList.getTransactions().isEmpty()) {
            transactions = bizEventsTransactionsList
                    .getTransactions()
                    .stream()
                    .map(transactionDTOMapper::apply)
                    .toList();
        } else {
            transactions = new ArrayList<>();
        }
        return transactionsListDTOMapper.apply(transactions, size);
    }

    @Override
    public TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId) {
        String retrievedUserFiscalCode = SecurityUtils.getUserFiscalCode();
        BizEventsTransactionDetailsDTO bizEventsTransactionDetails = bizEventsConnector.getTransactionDetails(retrievedUserFiscalCode, transactionId);
        return transactionDetailsDTOMapper.apply(bizEventsTransactionDetails);
    }

    @Override
    public Resource retrieveTransactionReceiptFromBizEvents(String transactionId) {
        String retrievedUserFiscalCode = SecurityUtils.getUserFiscalCode();
        return bizEventsConnector.getTransactionReceipt(retrievedUserFiscalCode, transactionId);
    }

    @Override
    public NoticesListResponseDTO retrievePaidListFromBizEvents(String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering) {
        NoticesListResponseDTO noticesListResponseDTO;
        String retrievedContinuationToken = null;

        String retrievedUserFiscalCode = SecurityUtils.getUserFiscalCode();

        BizEventsPaidResponseDTO paidResponseDTO = bizEventsPaidNoticeConnector.getPaidNoticeList(retrievedUserFiscalCode, continuationToken, size, isPayer, isDebtor, orderBy, ordering);

        if(paidResponseDTO != null){
            if (StringUtils.isNotBlank(paidResponseDTO.getContinuationToken())){
                retrievedContinuationToken = paidResponseDTO.getContinuationToken();
            }

            if(!paidResponseDTO.getNotices().isEmpty()){
                noticesListResponseDTO= noticesListResponseDTOMapper.mapToFullResponse(bizEventsPaidNoticeDTO2NoticeDTOMapper, bizEventsPaidNoticeListDTO2NoticesListDTOMapper, paidResponseDTO.getNotices(), retrievedContinuationToken);
            }else {
                noticesListResponseDTO = NoticesListResponseDTO.builder()
                        .noticesListDTO(new NoticesListDTO())
                        .build();
            }

        }else {
            noticesListResponseDTO = NoticesListResponseDTO.builder()
                    .noticesListDTO(new NoticesListDTO())
                    .build();
        }

        return noticesListResponseDTO;
    }
}
