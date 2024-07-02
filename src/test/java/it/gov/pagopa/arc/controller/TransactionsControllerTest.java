package it.gov.pagopa.arc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcTransactionsApi;
import it.gov.pagopa.arc.fakers.TransactionDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.TransactionsService;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {
        ArcTransactionsApi.class
})
class TransactionsControllerTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final String FILTER = "DUMMY_FILTER";
    private static final String TRANSACTION_ID = "TRANSACTION_ID";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionsService transactionsServiceMock;

    @Test
    void givenFiscalCodeWhenCallGetTransactionsListThenReturnTransactionList() throws Exception {
        //Given
        TransactionsListDTO transactionsListDTO = TransactionsListDTO.builder().build();
        Mockito.when(transactionsServiceMock.retrieveTransactionsList(PAGE,SIZE,FILTER)).thenReturn(transactionsListDTO);

        //When
        MvcResult result = mockMvc.perform(
                get("/arc/transactions")
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(SIZE))
                        .param("filter", FILTER)
        ).andExpect(status().is2xxSuccessful())
                .andReturn();

        TransactionsListDTO resultResponse = TestUtils.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TransactionsListDTO.class);

        //Then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(transactionsListDTO,resultResponse);
        Mockito.verify(transactionsServiceMock).retrieveTransactionsList(anyInt(),anyInt(),anyString());
    }

    @Test
    void givenTransactionIdWhenCallGetTransactionDetailsThenReturnTransactionDetails() throws Exception {
        //Given
        TransactionDetailsDTO transactionDetailsDTO = TransactionDetailsDTOFaker.mockInstance();

        Mockito.when(transactionsServiceMock.retrieveTransactionDetails(TRANSACTION_ID)).thenReturn(transactionDetailsDTO);

        //When
        MvcResult result = mockMvc.perform(
                        get("/arc/transactions/{transactionId}", TRANSACTION_ID)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        TransactionDetailsDTO resultResponse = TestUtils.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TransactionDetailsDTO.class);

        //Then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(transactionDetailsDTO,resultResponse);
        Mockito.verify(transactionsServiceMock).retrieveTransactionDetails(anyString());
    }
}