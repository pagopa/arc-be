package it.gov.pagopa.arc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.dto.TransactionDTO;
import it.gov.pagopa.arc.service.TransactionsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utils.TestUtils;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = {
        TransactionsController.class
})
class TransactionsControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionsService transactionsServiceMock;

    @Test
    void givenFiscalCodeWhenCallGetTransactionsListThenReturnTransactionList() throws Exception {
        //Given
        ZonedDateTime transactionDate = ZonedDateTime.parse("2024-05-09T14:44:22.854Z");
        List<TransactionDTO> transactionDTOList = List.of(
                new TransactionDTO("TRX_ID","PAYEE_TAX_CODE","20.80",transactionDate,false,true,true));

        Mockito.when(transactionsServiceMock.retrieveTransactionsList("DUMMY_FISCAL_CODE")).thenReturn(transactionDTOList);
        System.out.println(transactionDTOList);
        //When
        MvcResult result = mockMvc.perform(get("/arc/transactions")
                .header("x-fiscal-code", "DUMMY_FISCAL_CODE")
        ).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].transactionDate").value("2024-05-09T14:44:22.854Z"))
                .andReturn();

        List<TransactionDTO> resultResponse = TestUtils.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        //Then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(transactionDTOList,resultResponse);
        Mockito.verify(transactionsServiceMock).retrieveTransactionsList(anyString());
    }
}