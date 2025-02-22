package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NoticeRequestDTOMapperTest {
    private static final int SIZE = 2;
    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String ORDER_BY = "TRANSACTION_DATE";
    private static final String ORDERING = "DESC";
    NoticeRequestDTOMapper noticeRequestDTOMapper = new NoticeRequestDTOMapper();
    @Test
    void givenParametersWhenCallApplyThenReturnNoticeRequestDTO() {
        //given
        //when
        NoticeRequestDTO requestDTO = noticeRequestDTOMapper.apply(CONTINUATION_TOKEN, SIZE, true, true, ORDER_BY, ORDERING);
        //then
        Assertions.assertEquals(CONTINUATION_TOKEN, requestDTO.getContinuationToken());
        Assertions.assertEquals(SIZE, requestDTO.getSize());
        Assertions.assertEquals(true, requestDTO.getPaidByMe());
        Assertions.assertEquals(true, requestDTO.getRegisteredToMe());
        Assertions.assertEquals(ORDER_BY, requestDTO.getOrderBy());
        Assertions.assertEquals(ORDERING, requestDTO.getOrdering());

        TestUtils.assertNotNullFields(requestDTO);
    }

    @Test
    void givenSomeParametersNullWhenCallApplyThenReturnNoticeRequestDTO() {
        //given
        //when
        NoticeRequestDTO requestDTO = noticeRequestDTOMapper.apply(null, SIZE, null, true, null, ORDERING);
        //then
        TestUtils.assertNotNullFields(requestDTO, "continuationToken", "paidByMe", "orderBy");
    }
}