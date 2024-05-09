package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.dto.TransactionDTO;
import it.gov.pagopa.arc.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionsServiceImpl implements TransactionsService {
    private final ObjectMapper objectMapper;

    public TransactionsServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<TransactionDTO> retrieveTransactionsList(String fiscalCode) {
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        if (StringUtils.isNotBlank(fiscalCode)) {
            try {
                InputStream mockedTransactionsListJson = TransactionDTO.class.getResourceAsStream("/mock/transactions_response.json");
                List<UserDTO> userDTOList = objectMapper.readValue(mockedTransactionsListJson, new TypeReference<>() {
                });
                transactionDTOList = userDTOList.stream()
                        .filter(u -> u.getTaxCode().equals(fiscalCode))
                        .map(UserDTO::getTransactions)
                        .flatMap(List::stream)
                        .toList();

            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }
        return transactionDTOList;
    }
}
