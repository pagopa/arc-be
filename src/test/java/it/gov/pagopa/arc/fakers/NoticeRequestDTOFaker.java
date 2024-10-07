package it.gov.pagopa.arc.fakers;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.utils.TestUtils;

public class NoticeRequestDTOFaker {
    private static final int SIZE = 2;
    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String ORDER_BY = "TRANSACTION_DATE";
    private static final String ORDERING = "DESC";
    public static NoticeRequestDTO mockInstance (){
        return mockInstanceBuilder().build();
    }
    private static NoticeRequestDTO.NoticeRequestDTOBuilder mockInstanceBuilder(){
        NoticeRequestDTO.NoticeRequestDTOBuilder noticeRequestBuilder = NoticeRequestDTO.
                builder()
                .continuationToken(CONTINUATION_TOKEN)
                .paidByMe(true)
                .registeredToMe(true)
                .size(SIZE)
                .orderBy(ORDER_BY)
                .ordering(ORDERING);

        TestUtils.assertNotNullFields(noticeRequestBuilder);
        return noticeRequestBuilder;
    }
}
