package it.gov.pagopa.arc.controller;


import it.gov.pagopa.arc.dto.TransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/arc")
public interface TransactionsController {
    @GetMapping("/transactions")
    List<TransactionDTO> getTransactionsList(
            @RequestHeader(value = "x-fiscal-code") String fiscalCode,
            @RequestHeader(value = "x-continuation-token", required = false) String continuationToken,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    );
}
