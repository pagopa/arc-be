package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsUserDetailDTO;
import it.gov.pagopa.arc.fakers.CommonUserDetailDTOFaker;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BizEventsUserDetail2UserDetailDTOTest {

    private BizEventsUserDetail2UserDetailDTO userDetailMapper;

    @BeforeEach
    void setUp() {
        userDetailMapper = new BizEventsUserDetail2UserDetailDTO();
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

    }
}