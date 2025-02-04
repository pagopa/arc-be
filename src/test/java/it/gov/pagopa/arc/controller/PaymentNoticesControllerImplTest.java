package it.gov.pagopa.arc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticesApi;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.connector.PaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.PaymentNoticesService;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private static final String IUPD = "IUPD";
    private static final String PA_TAX_CODE = "DUMMY_ORGANIZATION_FISCAL_CODE";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
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

    @Test
    void givenPaTaxCodeAndIUPDWhenGetPaymentNoticeDetailsThenReturnPaymentNoticeDetails() throws Exception {
        //given
        PaymentNoticeDetailsDTO paymentNoticeDetailsDTO = PaymentNoticeDetailsDTOFaker.mockInstance(1, false);
        Mockito.when(paymentNoticesServiceMock.retrievePaymentNoticeDetails(USER_ID, PA_TAX_CODE, IUPD)).thenReturn(paymentNoticeDetailsDTO);
        //When
        MvcResult result = mockMvc.perform(
                        get("/payment-notices/{iupd}", IUPD)
                                .param("paTaxCode", PA_TAX_CODE)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        PaymentNoticeDetailsDTO resultResponse = TestUtils.objectMapper.readValue(result.getResponse().getContentAsString(),
                PaymentNoticeDetailsDTO.class);
        //then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(paymentNoticeDetailsDTO, resultResponse);
        Assertions.assertEquals(1, resultResponse.getPaymentOptions().size());
    }

    @Test
    void givenPaTaxCodeAndIUPDWhenGetPaymentNoticeDetailsThenReturnPaymentNoticeDetailsWithInstallments() throws Exception {
        //given
        PaymentNoticeDetailsDTO paymentNoticeDetailsDTO = PaymentNoticeDetailsDTOFaker.mockInstance(2, true);
        Mockito.when(paymentNoticesServiceMock.retrievePaymentNoticeDetails(USER_ID, PA_TAX_CODE, IUPD)).thenReturn(paymentNoticeDetailsDTO);
        //When
        MvcResult result = mockMvc.perform(
                        get("/payment-notices/{iupd}", IUPD)
                                .param("paTaxCode", PA_TAX_CODE)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        PaymentNoticeDetailsDTO resultResponse = TestUtils.objectMapper.readValue(result.getResponse().getContentAsString(),
                PaymentNoticeDetailsDTO.class);
        //then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(paymentNoticeDetailsDTO, resultResponse);
        Assertions.assertEquals(2, resultResponse.getPaymentOptions().size());
    }

}