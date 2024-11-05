package it.gov.pagopa.arc.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcTransactionsApi;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.TransactionsService;
import it.gov.pagopa.arc.utils.TestUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(value = {
        ArcTransactionsApi.class
},excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
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
                get("/transactions")
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
    void givenTransactionIdWhenCallGetTransactionReceiptThenReturnTransactionReceipt() throws Exception {
        //Given
        Resource receipt = new FileSystemResource("src/test/resources/stub/__files/testReceiptPdfFile.pdf");

        Mockito.when(transactionsServiceMock.retrieveTransactionReceipt(TRANSACTION_ID)).thenReturn(receipt);

        //When
        MvcResult result = mockMvc.perform(
                        get("/transactions/{transactionId}/receipt", TRANSACTION_ID)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] actualContent = result.getResponse().getContentAsByteArray();

        Assertions.assertNotNull(actualContent);
        Assertions.assertArrayEquals(expectedContent, actualContent);
        Mockito.verify(transactionsServiceMock).retrieveTransactionReceipt(anyString());
    }
}