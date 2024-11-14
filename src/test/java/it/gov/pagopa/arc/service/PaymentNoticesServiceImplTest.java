package it.gov.pagopa.arc.service;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.service.pullpayment.PullPaymentService;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentNoticesServiceImplTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final LocalDate DUE_DATE = LocalDate.parse("2024-04-11");
    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final String USER_ID = "user_id";

    @Mock
    private PullPaymentService pullPaymentServiceMock;

    private MemoryAppender memoryAppender;
    private PaymentNoticesService paymentNoticesService;

    @BeforeEach
    void setUp() {
        paymentNoticesService = new PaymentNoticesServiceImpl(pullPaymentServiceMock);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.service.PaymentNoticesServiceImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenParametersWhenRetrievePaymentNoticesThenReturnPaymentNoticesListDTO() {
        //given
        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO2 = PaymentNoticeDTOFaker.mockInstance(true);

        List<PaymentNoticeDTO> paymentNoticesList = List.of(paymentNoticeDTO1, paymentNoticeDTO2);

        PaymentNoticesListDTO paymentNoticesListDTO = PaymentNoticesListDTO.builder().paymentNotices(paymentNoticesList).build();

        Mockito.when(pullPaymentServiceMock.retrievePaymentNoticesListFromPullPayment(DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE)).thenReturn(paymentNoticesListDTO);
        //when
        PaymentNoticesListDTO result = paymentNoticesService.retrievePaymentNotices(USER_ID, DUMMY_FISCAL_CODE, DUE_DATE, SIZE, PAGE);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(paymentNoticesList, result.getPaymentNotices());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[GET_PAYMENT_NOTICES] The current user with user id : user_id, has requested to retrieve payment notices, with the current parameters: dueDate 2024-04-11, size 2 and page 1"));
        Mockito.verify(pullPaymentServiceMock).retrievePaymentNoticesListFromPullPayment(anyString(), any(), anyInt(), anyInt());
    }
}