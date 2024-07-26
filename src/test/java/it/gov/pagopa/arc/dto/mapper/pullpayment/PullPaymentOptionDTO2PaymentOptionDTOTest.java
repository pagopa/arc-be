package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentInstallmentDTOFaker;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentOptionDTOFaker;
import it.gov.pagopa.arc.fakers.paymentNotices.InstallmentDTOFaker;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import it.gov.pagopa.arc.model.generated.PaymentOptionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PullPaymentOptionDTO2PaymentOptionDTOTest {
    @Mock
    private PullPaymentInstallmentDTO2InstallmentDTO pullPaymentInstallmentDTO2InstallmentDTOMock;

    @InjectMocks
    private PullPaymentOptionDTO2PaymentOptionDTOImpl mapper;
    @Test
    void givenPullPaymentOptionDTOWhenCallMapperThenReturnPaymentOptionDTO() {
        //given
        PullPaymentInstallmentDTO pullPaymentInstallmentDTO = PullPaymentInstallmentDTOFaker.mockInstance();
        PullPaymentOptionDTO pullPaymentOptionDTO = PullPaymentOptionDTOFaker.mockInstance(pullPaymentInstallmentDTO, false);
        InstallmentDTO installmentDTO = InstallmentDTOFaker.mockInstance();

        Mockito.when(pullPaymentInstallmentDTO2InstallmentDTOMock.toInstallmentDTO(pullPaymentInstallmentDTO)).thenReturn(installmentDTO);
        //when
        PaymentOptionDTO result = mapper.toPaymentOptionDTO(pullPaymentOptionDTO);
        //then
        Assertions.assertAll(() -> {
            Assertions.assertNotNull(result);
            Assertions.assertEquals("Test Pull - unica opzione", result.getDescription());
            Assertions.assertEquals(1, result.getNumberOfInstallments());
            Assertions.assertEquals(120L, result.getAmount());
            Assertions.assertEquals(LocalDateTime.parse("2024-10-30T23:59:59"), result.getDueDate());
            Assertions.assertFalse(result.getIsPartialPayment());
            Assertions.assertFalse(result.getSwitchToExpired());
            Assertions.assertEquals(List.of(installmentDTO), result.getInstallments());

            Mockito.verify(pullPaymentInstallmentDTO2InstallmentDTOMock).toInstallmentDTO(any());
        });

    }
}