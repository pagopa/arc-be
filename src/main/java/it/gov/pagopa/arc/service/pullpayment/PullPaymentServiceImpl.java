package it.gov.pagopa.arc.service.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.PullPaymentConnector;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PaymentNoticesListDTOMapper;
import it.gov.pagopa.arc.dto.mapper.pullpayment.PullPaymentNoticeDTO2PaymentNoticeDTOMapper;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PullPaymentServiceImpl implements PullPaymentService{
    private final PullPaymentConnector pullPaymentConnector;
    private final PullPaymentNoticeDTO2PaymentNoticeDTOMapper paymentNoticeDTOMapper;
    private final PaymentNoticesListDTOMapper paymentNoticesListDTOMapper;

    public PullPaymentServiceImpl(PullPaymentConnector pullPaymentConnector,
                                  PullPaymentNoticeDTO2PaymentNoticeDTOMapper paymentNoticeDTOMapper,
                                  PaymentNoticesListDTOMapper paymentNoticesListDTOMapper) {
        this.pullPaymentConnector = pullPaymentConnector;
        this.paymentNoticeDTOMapper = paymentNoticeDTOMapper;
        this.paymentNoticesListDTOMapper = paymentNoticesListDTOMapper;
    }

    @Override
    public PaymentNoticesListDTO retrievePaymentNoticesListFromPullPayment(String userFiscalCode, LocalDate dueDate, Integer size, Integer page) {
        List<PullPaymentNoticeDTO> pullPaymentNoticeDTOList = pullPaymentConnector.getPaymentNotices(userFiscalCode, dueDate, size, page);
        List<PaymentNoticeDTO> paymentNoticeDTOFilteredList;

        if(!pullPaymentNoticeDTOList.isEmpty()){
            paymentNoticeDTOFilteredList =
                    pullPaymentNoticeDTOList
                            .stream()
                            .filter(paymentNotice ->
                                    paymentNotice.getPaymentOptions().size() == 1 &&
                                            paymentNotice.getPaymentOptions().getFirst().getNumberOfInstallments().equals(1)
                            )
                            .map(paymentNoticeDTOMapper::toPaymentNoticeDTO)
                            .toList();
        }else {
            paymentNoticeDTOFilteredList = new ArrayList<>();
        }

        return paymentNoticesListDTOMapper.toPaymentNoticesListDTO(paymentNoticeDTOFilteredList);
    }
}
