package it.gov.pagopa.arc.service.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.PullPaymentConnector;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PaymentNoticesListDTOMapper;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PullPaymentNoticeDTO2PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PullPaymentServiceImpl implements PullPaymentService{
    private final String fakeFiscalCode;
    private final PullPaymentConnector pullPaymentConnector;
    private final PullPaymentNoticeDTO2PaymentNoticeDTO paymentNoticeDTOMapper;
    private final PaymentNoticesListDTOMapper paymentNoticesListDTOMapper;

    public PullPaymentServiceImpl(@Value("${rest-client.fake-fiscal-code}") String fakeFiscalCode,
                                  PullPaymentConnector pullPaymentConnector,
                                  PullPaymentNoticeDTO2PaymentNoticeDTO paymentNoticeDTOMapper,
                                  PaymentNoticesListDTOMapper paymentNoticesListDTOMapper) {
        this.fakeFiscalCode = fakeFiscalCode;
        this.pullPaymentConnector = pullPaymentConnector;
        this.paymentNoticeDTOMapper = paymentNoticeDTOMapper;
        this.paymentNoticesListDTOMapper = paymentNoticesListDTOMapper;
    }

    @Override
    public PaymentNoticesListDTO retrievePaymentNoticesListFromPullPayment(LocalDate dueDate, Integer size, Integer page) {
        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = pullPaymentConnector.getPaymentNotices(fakeFiscalCode, dueDate, size, page);
        List<PaymentNoticeDTO> paymentNoticeDTOFilteredList;

        if(!pullPaymentNoticeDTOList.isEmpty()){
            paymentNoticeDTOFilteredList =
                    pullPaymentNoticeDTOList
                            .stream()
                            .filter(paymentNotice ->
                                    paymentNotice.getPaymentOptions().size() == 1 &&
                                            paymentNotice.getPaymentOptions().get(0).getNumberOfInstallments().equals(1)
                            )
                            .map(paymentNoticeDTOMapper::toPaymentNoticeDTO)
                            .toList();
        }else {
            paymentNoticeDTOFilteredList = new ArrayList<>();
        }

        return paymentNoticesListDTOMapper.toPaymentNoticesListDTO(paymentNoticeDTOFilteredList);
    }
}
