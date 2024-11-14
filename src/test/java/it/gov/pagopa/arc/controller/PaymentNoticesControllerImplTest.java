package it.gov.pagopa.arc.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticesApi;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.PaymentNoticesService;
import it.gov.pagopa.arc.utils.TestUtils;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final String USER_ID = "user_id";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentNoticesService paymentNoticesServiceMock;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                IamUserInfoDTOFaker.mockInstance(), null, null);
        authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenFiscalCodeWhenGetPaymentNoticesThenReturnPaymentNoticesList() throws Exception {
        //given
        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO2 = PaymentNoticeDTOFaker.mockInstance(true);
        List<PaymentNoticeDTO> listOfPaymentNoticeDto = List.of(paymentNoticeDTO1, paymentNoticeDTO2);

        PaymentNoticesListDTO paymentNoticesListDTO = PaymentNoticesListDTO.builder()
                .paymentNotices(listOfPaymentNoticeDto)
                .build();

        Mockito.when(paymentNoticesServiceMock.retrievePaymentNotices(USER_ID, DUMMY_FISCAL_CODE, DUE_DATE,SIZE,PAGE)).thenReturn(paymentNoticesListDTO);
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
        Mockito.verify(paymentNoticesServiceMock).retrievePaymentNotices(anyString(), anyString(), any(),anyInt(),anyInt());
    }
}