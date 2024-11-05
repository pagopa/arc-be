package it.gov.pagopa.arc.controller;


import it.gov.pagopa.arc.controller.generated.ArcTransactionsApi;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.TransactionsService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionsControllerImpl implements ArcTransactionsApi {
    private final TransactionsService transactionsService;

    public TransactionsControllerImpl(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @Override
    public ResponseEntity<TransactionsListDTO> getTransactionsList(Integer page, Integer size, String filter) {
        TransactionsListDTO transactionsListDTO = transactionsService.retrieveTransactionsList(page,size,filter);
        return new ResponseEntity<>(transactionsListDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getTransactionReceipt(String transactionId) {
        Resource receipt = transactionsService.retrieveTransactionReceipt(transactionId);
        return new ResponseEntity<>(receipt,HttpStatus.OK);
    }

}
