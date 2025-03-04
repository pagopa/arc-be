package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsWalletInfoDTO;
import it.gov.pagopa.arc.fakers.CommonWalletInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.WalletInfoDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BizEventsWalletInfo2WalletInfoDTOMapperTest {
    private final BizEventsWalletInfo2WalletInfoDTOMapper walletInfoMapper = Mappers.getMapper(BizEventsWalletInfo2WalletInfoDTOMapper.class);

    @Test
    void givenBizEventsWalletInfoDTOWhenMapWalletInfoThenReturnWalletInfoDTO() {
        //given
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(true);
        //when
        WalletInfoDTO result = walletInfoMapper.mapToWalletInfo(bizEventsWalletInfo);
        //then
        assertNotNull(result);
        assertEquals("USER_HOLDER", result.getAccountHolder());
        assertEquals(WalletInfoDTO.BrandEnum.VISA, result.getBrand());
        assertEquals("0932", result.getBlurredNumber());
        assertEquals("user@paypal.com", result.getMaskedEmail());
        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenNullBizEventsWalletInfoDTOWhenMapWalletInfoThenReturnNullWalletInfoDTO() {
        //when
        WalletInfoDTO result = walletInfoMapper.mapToWalletInfo(null);
        //then
        assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("provideEnumMappings")
    void givenWrongBrandWhenMapWalletInfoThenReturnEnum(String stringEnum, WalletInfoDTO.BrandEnum walletInfoEnum) {
        //given
        BizEventsWalletInfoDTO bizEventsWalletInfo = CommonWalletInfoDTOFaker.mockBizEventsWalletInfoDTO(false);
        bizEventsWalletInfo.setBrand(stringEnum);
        //when
        WalletInfoDTO result = walletInfoMapper.mapToWalletInfo(bizEventsWalletInfo);
        //then
        assertNotNull(result);
        assertEquals(walletInfoEnum, result.getBrand());
    }

    private static Stream<Arguments> provideEnumMappings() {
        return Stream.of(
                Arguments.of("AMEX", WalletInfoDTO.BrandEnum.AMEX),
                Arguments.of("VISA", WalletInfoDTO.BrandEnum.VISA),
                Arguments.of("JCB", WalletInfoDTO.BrandEnum.JCB),
                Arguments.of("DINERS", WalletInfoDTO.BrandEnum.DINERS),
                Arguments.of("DISCOVER", WalletInfoDTO.BrandEnum.DISCOVER),
                Arguments.of("MAESTRO", WalletInfoDTO.BrandEnum.MAESTRO),
                Arguments.of("MASTERCARD", WalletInfoDTO.BrandEnum.MASTERCARD),
                Arguments.of("UNIONPAY", WalletInfoDTO.BrandEnum.UNIONPAY),
                Arguments.of("OTHER", WalletInfoDTO.BrandEnum.OTHER),
                Arguments.of("UNKNOW_BRAND", WalletInfoDTO.BrandEnum.OTHER)
        );
    }
}