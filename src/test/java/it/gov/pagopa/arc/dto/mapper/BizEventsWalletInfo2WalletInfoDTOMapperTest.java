package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BizEventsWalletInfo2WalletInfoDTOMapperTest {
    private BizEventsWalletInfo2WalletInfoDTOMapper walletInfoMapper;

    @BeforeEach
    void setUp() {
        walletInfoMapper = new BizEventsWalletInfo2WalletInfoDTOMapper();
    }

    @Test
    void givenBizEventsWalletInfoDTOWhenMapWalletInfoThenReturnWalletInfoDTO() {
        //given
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(true);
        //when
        WalletInfoDTO result = walletInfoMapper.mapWalletInfo(bizEventsWalletInfo);
        //then
        assertNotNull(result);
        assertEquals("USER_HOLDER", result.getAccountHolder());
        assertEquals("VISA", result.getBrand());
        assertEquals("0932", result.getBlurredNumber());
        assertEquals("user@paypal.com", result.getMaskedEmail());
    }
}