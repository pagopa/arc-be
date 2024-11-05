package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnector;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTOMapper;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.utils.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class BizEventsServiceImpl implements BizEventsService{

    private final BizEventsConnector bizEventsConnector;
    private final BizEventsTransactionDTO2TransactionDTOMapper transactionDTOMapper;
    private final BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapper;
    private final BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector;
    private final BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;

    public BizEventsServiceImpl(BizEventsConnector bizEventsConnector,
                                BizEventsTransactionDTO2TransactionDTOMapper transactionDTOMapper,
                                BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapper,
                                BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector,
                                BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper) {
        this.bizEventsConnector = bizEventsConnector;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionsListDTOMapper = transactionsListDTOMapper;
        this.bizEventsPaidNoticeConnector = bizEventsPaidNoticeConnector;
        this.bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper = bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;
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
    public NoticesListResponseDTO retrievePaidListFromBizEvents(String userFiscalCode, NoticeRequestDTO noticeRequestDTO) {
         return bizEventsPaidNoticeConnector.getPaidNoticeList(userFiscalCode, noticeRequestDTO);
    }

    @Override
    public NoticeDetailsDTO retrievePaidNoticeDetailsFromBizEvents(String userId, String userFiscalCode, String eventId) {
        BizEventsPaidNoticeDetailsDTO paidNoticeDetails = bizEventsPaidNoticeConnector.getPaidNoticeDetails(userId, userFiscalCode, eventId);
        return bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper.toNoticeDetailsDTO(paidNoticeDetails);
    }

    @Override
    public Resource retrievePaidNoticeReceiptFromBizEvents(String userId, String userFiscalCode, String eventId) {
        return bizEventsPaidNoticeConnector.getPaidNoticeReceipt(userId, userFiscalCode, eventId);
    }
}
