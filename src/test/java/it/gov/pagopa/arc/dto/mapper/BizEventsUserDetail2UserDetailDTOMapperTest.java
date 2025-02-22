package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BizEventsUserDetail2UserDetailDTOMapperTest {

    private BizEventsUserDetail2UserDetailDTOMapper userDetailMapper;

    @BeforeEach
    void setUp() {
        userDetailMapper = new BizEventsUserDetail2UserDetailDTOMapper();
    }

    @Test
    void givenBizEventsUserDetailDTOWhenMapUserDetailThenReturnUserDetailDTO() {
        //given
        BizEventsUserDetailDTO payee = CommonUserDetailDTOFaker.mockBizEventsUserDetailDTO(CommonUserDetailDTOFaker.USER_DETAIL_PAYEE);
        //when
        UserDetailDTO result = userDetailMapper.mapUserDetail(payee);
        //then
        assertNotNull(result);
        assertEquals("CREDITOR_NAME", result.getName());
        assertEquals("CREDITOR_TAX_CODE", result.getTaxCode());
        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenNullBizEventsUserDetailDTOWhenMapUserDetailThenReturnNullUserDetailDTO() {
        //when
        UserDetailDTO result = userDetailMapper.mapUserDetail(null);
        //then
        assertNull(result);
    }
}