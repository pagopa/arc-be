package it.gov.pagopa.arc.service.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.PullPaymentConnector;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PaymentNoticesListDTOMapper;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PullPaymentNoticeDTO2PaymentNoticeDTOMapper;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentInstallmentDTOFaker;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentNoticeDTOFaker;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentOptionDTOFaker;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PullPaymentServiceImplTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final LocalDate DUE_DATE = LocalDate.now();

    @Autowired
    private PullPaymentService pullPaymentService;

    @Mock
    private PullPaymentConnector pullPaymentConnectorMock;
    @Mock
    private PullPaymentNoticeDTO2PaymentNoticeDTOMapper paymentNoticeDTOMapperMock;
    @Mock
    private PaymentNoticesListDTOMapper paymentNoticesListDTOMapperMock;

    @BeforeEach
    void setUp() {
        pullPaymentService = new PullPaymentServiceImpl(pullPaymentConnectorMock, paymentNoticeDTOMapperMock, paymentNoticesListDTOMapperMock);
    }

    @Test
    void givenParametersWhenRetrievePaymentNoticesListFromPullPaymentThenReturnPaymentNoticesListDTO() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO1 = PullPaymentNoticeDTOFaker.mockInstance(true);
        PullPaymentNoticeDTO pullPaymentNoticeDTO2 = PullPaymentNoticeDTOFaker.mockInstance(true);

        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = List.of(pullPaymentNoticeDTO1, pullPaymentNoticeDTO2);

        Mockito.when(pullPaymentConnectorMock.getPaymentNotices(DUMMY_FISCAL_CODE,DUE_DATE,SIZE,PAGE)).thenReturn(pullPaymentNoticeDTOList);

        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO2 = PaymentNoticeDTOFaker.mockInstance(true);

        List<PaymentNoticeDTO> paymentNoticesList = List.of(paymentNoticeDTO1, paymentNoticeDTO2);

        PaymentNoticesListDTO paymentNoticesListExpected = PaymentNoticesListDTO
                .builder()
                .paymentNotices(
                        paymentNoticesList)
                .build();

        Mockito.when(paymentNoticeDTOMapperMock.toPaymentNoticeDTO(pullPaymentNoticeDTO1)).thenReturn(paymentNoticeDTO1);
        Mockito.when(paymentNoticeDTOMapperMock.toPaymentNoticeDTO(pullPaymentNoticeDTO2)).thenReturn(paymentNoticeDTO2);

        Mockito.when(paymentNoticesListDTOMapperMock.toPaymentNoticesListDTO(paymentNoticesList)).thenReturn(paymentNoticesListExpected);
        //when
        PaymentNoticesListDTO result = pullPaymentService.retrievePaymentNoticesListFromPullPayment(DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getPaymentNotices().size());
        Assertions.assertEquals(paymentNoticesList, result.getPaymentNotices());

        assertThat(result.getPaymentNotices()).allMatch(paymentNotice -> paymentNotice.getPaymentOptions().size() == 1);

        Mockito.verify(pullPaymentConnectorMock).getPaymentNotices(anyString(),any(),anyInt(),anyInt());
        Mockito.verify(paymentNoticeDTOMapperMock, Mockito.times(2)).toPaymentNoticeDTO(any());
        Mockito.verify(paymentNoticesListDTOMapperMock).toPaymentNoticesListDTO(anyList());

    }

    @Test
    void givenParametersWhenRetrievePaymentNoticesListFromPullPaymentWithMultiplePaymentOptionThenReturnFilteredPaymentNoticesListDTO() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO1 = PullPaymentNoticeDTOFaker.mockInstance(true);
        PullPaymentNoticeDTO pullPaymentNoticeDTO2 = PullPaymentNoticeDTOFaker.mockInstance(false);

        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = List.of(pullPaymentNoticeDTO1, pullPaymentNoticeDTO2);

        Mockito.when(pullPaymentConnectorMock.getPaymentNotices(DUMMY_FISCAL_CODE,DUE_DATE,SIZE,PAGE)).thenReturn(pullPaymentNoticeDTOList);

        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);


        List<PaymentNoticeDTO> paymentNoticesList = List.of(paymentNoticeDTO1);

        PaymentNoticesListDTO paymentNoticesListExpected = PaymentNoticesListDTO
                .builder()
                .paymentNotices(
                        paymentNoticesList)
                .build();

        Mockito.when(paymentNoticeDTOMapperMock.toPaymentNoticeDTO(pullPaymentNoticeDTO1)).thenReturn(paymentNoticeDTO1);

        Mockito.when(paymentNoticesListDTOMapperMock.toPaymentNoticesListDTO(paymentNoticesList)).thenReturn(paymentNoticesListExpected);
        //when
        PaymentNoticesListDTO result = pullPaymentService.retrievePaymentNoticesListFromPullPayment(DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getPaymentNotices().size());
        Assertions.assertEquals(paymentNoticesList, result.getPaymentNotices());

        assertThat(result.getPaymentNotices()).allMatch(paymentNotice -> paymentNotice.getPaymentOptions().size() == 1);

        Mockito.verify(pullPaymentConnectorMock).getPaymentNotices(anyString(),any(),anyInt(),anyInt());
        Mockito.verify(paymentNoticeDTOMapperMock).toPaymentNoticeDTO(any());
        Mockito.verify(paymentNoticesListDTOMapperMock).toPaymentNoticesListDTO(anyList());

    }

    @Test
    void givenParametersWhenRetrievePaymentNoticesListFromPullPaymentWithMultipleNumberOfInstallmentsThenReturnFilteredPaymentNoticesListDTO() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO1 = PullPaymentNoticeDTOFaker.mockInstance(true);
        PullPaymentNoticeDTO pullPaymentNoticeDTO2 = PullPaymentNoticeDTOFaker.mockInstance(true);

        PullPaymentInstallmentDTO pullPaymentInstallmentDTO = PullPaymentInstallmentDTOFaker.mockInstance();
        PullPaymentOptionDTO pullPaymentOptionDTO = PullPaymentOptionDTOFaker.mockInstance(pullPaymentInstallmentDTO, true);
        pullPaymentNoticeDTO1.setPaymentOptions(List.of(pullPaymentOptionDTO));

        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = List.of(pullPaymentNoticeDTO1, pullPaymentNoticeDTO2);

        Mockito.when(pullPaymentConnectorMock.getPaymentNotices(DUMMY_FISCAL_CODE,DUE_DATE,SIZE,PAGE)).thenReturn(pullPaymentNoticeDTOList);

        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);


        List<PaymentNoticeDTO> paymentNoticesList = List.of(paymentNoticeDTO1);

        PaymentNoticesListDTO paymentNoticesListExpected = PaymentNoticesListDTO
                .builder()
                .paymentNotices(
                        paymentNoticesList)
                .build();

        Mockito.when(paymentNoticeDTOMapperMock.toPaymentNoticeDTO(pullPaymentNoticeDTO2)).thenReturn(paymentNoticeDTO1);

        Mockito.when(paymentNoticesListDTOMapperMock.toPaymentNoticesListDTO(paymentNoticesList)).thenReturn(paymentNoticesListExpected);
        //when
        PaymentNoticesListDTO result = pullPaymentService.retrievePaymentNoticesListFromPullPayment(DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getPaymentNotices().size());
        Assertions.assertEquals(paymentNoticesList, result.getPaymentNotices());

        assertThat(result.getPaymentNotices()).allMatch(paymentNotice -> paymentNotice.getPaymentOptions().stream().allMatch(paymentOption -> paymentOption.getNumberOfInstallments() == 1));

        Mockito.verify(pullPaymentConnectorMock).getPaymentNotices(anyString(),any(),anyInt(),anyInt());
        Mockito.verify(paymentNoticeDTOMapperMock).toPaymentNoticeDTO(any());
        Mockito.verify(paymentNoticesListDTOMapperMock).toPaymentNoticesListDTO(anyList());

    }

    @Test
    void givenParametersWhenRetrieveEmptyPaymentNoticesListFromPullPaymentThenReturnEmptyList() {
        //given
        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = new ArrayList<>();

        Mockito.when(pullPaymentConnectorMock.getPaymentNotices(DUMMY_FISCAL_CODE,DUE_DATE,SIZE,PAGE)).thenReturn(pullPaymentNoticeDTOList);

        List<PaymentNoticeDTO> paymentNoticesList = new ArrayList<>();

        PaymentNoticesListDTO paymentNoticesListExpected = PaymentNoticesListDTO
                .builder()
                .paymentNotices(
                        paymentNoticesList)
                .build();

        Mockito.when(paymentNoticesListDTOMapperMock.toPaymentNoticesListDTO(paymentNoticesList)).thenReturn(paymentNoticesListExpected);
        //when
        PaymentNoticesListDTO result = pullPaymentService.retrievePaymentNoticesListFromPullPayment(DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getPaymentNotices().isEmpty());
        Assertions.assertEquals(paymentNoticesList, result.getPaymentNotices());

        Mockito.verify(pullPaymentConnectorMock).getPaymentNotices(anyString(),any(),anyInt(),anyInt());
        Mockito.verify(paymentNoticesListDTOMapperMock).toPaymentNoticesListDTO(anyList());

    }

}