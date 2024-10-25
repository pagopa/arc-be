package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsCartItemDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsCartItemDTOFaker;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BizEventsCartItem2CartItemDTOMapperTest {

    @Mock
    private BizEventsUserDetail2UserDetailDTOMapper userDetailsMapperMock;

    @InjectMocks
    private final BizEventsCartItem2CartItemDTOMapper cartItemMapper = Mappers.getMapper(BizEventsCartItem2CartItemDTOMapper.class);

    @Test
    void givenBizEventsCartItemDTOWhenMapCartThenReturnCartItemDTO() {
        //given
        BizEventsUserDetailDTO payee = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);

        BizEventsUserDetailDTO debtor = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_DEBTOR);

        UserDetailDTO payeeResponse = UserDetailDTO.builder()
                .name("ACI Automobile Club Italia")
                .taxCode("00493410583")
                .build();

        UserDetailDTO debtorResponse = UserDetailDTO.builder()
                .name("DEBTOR")
                .taxCode("TAX_CODE")
                .build();

        Mockito.when(userDetailsMapperMock.mapUserDetail(payee)).thenReturn(payeeResponse);
        Mockito.when(userDetailsMapperMock.mapUserDetail(debtor)).thenReturn(debtorResponse);

        BizEventsCartItemDTO cartItemDTO = BizEventsCartItemDTOFaker.mockInstance(payee,debtor);
        //when
        CartItemDTO result = cartItemMapper.mapCart(cartItemDTO);
        //then
        assertNotNull(result);
        assertEquals("pagamento", result.getSubject());
        assertEquals(545230L, result.getAmount());
        assertEquals(payeeResponse, result.getPayee());
        assertEquals(debtorResponse, result.getDebtor());
        assertEquals("960000000094659945", result.getRefNumberValue());
        assertEquals("IUV", result.getRefNumberType());
        Mockito.verify(userDetailsMapperMock, Mockito.times(2)).mapUserDetail(any());
    }
}