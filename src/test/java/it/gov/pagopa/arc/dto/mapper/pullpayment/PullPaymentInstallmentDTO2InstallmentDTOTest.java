package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentOptionStatus;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentInstallmentDTOFaker;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import it.gov.pagopa.arc.model.generated.PaymentOptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PullPaymentInstallmentDTO2InstallmentDTOTest {

    @Mock
    private PullPaymentOptionStatus2PaymentOptionStatus pullPaymentOptionStatus2PaymentOptionStatusMock;

    @InjectMocks
    private final PullPaymentInstallmentDTO2InstallmentDTO mapper = Mappers.getMapper(PullPaymentInstallmentDTO2InstallmentDTO.class);

    @Test
    void givenPullPaymentInstallmentDTOWhenCallMapperThenReturnInstallmentDTO() {
        //given
        PullPaymentOptionStatus poUnpaid = PullPaymentOptionStatus.PO_UNPAID;
        PaymentOptionStatus unpaid = PaymentOptionStatus.UNPAID;

        Mockito.when(pullPaymentOptionStatus2PaymentOptionStatusMock.toPaymentOptionStatus(poUnpaid)).thenReturn(unpaid);

        PullPaymentInstallmentDTO pullPaymentInstallmentDTO = PullPaymentInstallmentDTOFaker.mockInstance();

        //when
        InstallmentDTO result = mapper.toInstallmentDTO(pullPaymentInstallmentDTO);

        //then
        Assertions.assertAll(() -> {
            Assertions.assertNotNull(result);
            Assertions.assertEquals("347000000880099993", result.getNav());
            Assertions.assertEquals("47000000880099993", result.getIuv());
            Assertions.assertEquals("99999000013", result.getPaTaxCode());
            Assertions.assertEquals("EC Demo Pagamenti Pull Test", result.getPaFullName());
            Assertions.assertEquals(120L, result.getAmount());
            Assertions.assertEquals("Test Pull - unica opzione", result.getDescription());
            Assertions.assertEquals(LocalDateTime.parse("2024-10-30T23:59:59"), result.getDueDate());
            Assertions.assertEquals(LocalDateTime.parse("2024-11-30T23:59:59"), result.getRetentionDate());
            Assertions.assertEquals(LocalDateTime.parse("2024-04-11T06:56:14.845126"), result.getInsertedDate());
            Assertions.assertEquals(0L, result.getNotificationFee());
            Assertions.assertEquals(unpaid, result.getStatus());
            Assertions.assertEquals(LocalDateTime.parse("2024-04-11T06:56:14.845126"), result.getLastUpdatedDate());

            Mockito.verify(pullPaymentOptionStatus2PaymentOptionStatusMock).toPaymentOptionStatus(any());
        });
    }
}