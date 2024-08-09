package it.gov.pagopa.arc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticesApi;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.PaymentNoticesService;
import it.gov.pagopa.arc.utils.TestUtils;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(value = {
        ArcPaymentNoticesApi.class
},excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class PaymentNoticesControllerImplTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final LocalDate DUE_DATE = LocalDate.now();

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentNoticesService paymentNoticesServiceMock;

    @Test
    void givenFiscalCodeWhenGetPaymentNoticesThenReturnPaymentNoticesList() throws Exception {
        //given
        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO2 = PaymentNoticeDTOFaker.mockInstance(true);
        List<PaymentNoticeDTO> listOfPaymentNoticeDto = List.of(paymentNoticeDTO1, paymentNoticeDTO2);

        PaymentNoticesListDTO paymentNoticesListDTO = PaymentNoticesListDTO.builder()
                .paymentNotices(listOfPaymentNoticeDto)
                .build();

        Mockito.when(paymentNoticesServiceMock.retrievePaymentNotices(DUE_DATE,SIZE,PAGE)).thenReturn(paymentNoticesListDTO);
        //when

        //When
        MvcResult result = mockMvc.perform(
                        get("/payment-notices")
                                .param("dueDate", String.valueOf(DUE_DATE))
                                .param("page", String.valueOf(PAGE))
                                .param("size", String.valueOf(SIZE))
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        PaymentNoticesListDTO resultResponse = TestUtils.objectMapper.readValue(result.getResponse().getContentAsString(),
                PaymentNoticesListDTO.class);
        //then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(paymentNoticesListDTO,resultResponse);
        Mockito.verify(paymentNoticesServiceMock).retrievePaymentNotices(any(),anyInt(),anyInt());
    }
}