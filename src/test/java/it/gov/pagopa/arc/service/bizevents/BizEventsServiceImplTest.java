package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnector;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.fakers.NoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.NoticeRequestDTOFaker;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.paidnotice.BizEventsPaidNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BizEventsServiceImplTest {

    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final String CONTINUATION_TOKEN = "continuation-token";

    private BizEventsService bizEventsService;

    @Mock
    private BizEventsPaidNoticeConnector bizEventsPaidNoticeConnectorMock;

    @Mock
    private BizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapper bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapperMock;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                IamUserInfoDTOFaker.mockInstance(), null, null);
        authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        bizEventsService = new BizEventsServiceImpl(
                bizEventsPaidNoticeConnectorMock,
                bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapperMock);
    }

    @AfterEach
    void afterMethod() {
        Mockito.verifyNoMoreInteractions(
                bizEventsPaidNoticeConnectorMock,
                bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapperMock
        );
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenParametersWhenCallRetrievePaidListFromBizEventsThenReturnPaidList() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        List<NoticeDTO> listOfNoticeDTO = List.of(noticeDTO);

        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(listOfNoticeDTO).build();

        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder().noticesListDTO(noticesListDTO).continuationToken(CONTINUATION_TOKEN).build();
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        when(bizEventsPaidNoticeConnectorMock.getPaidNoticeList(DUMMY_FISCAL_CODE, noticeRequestDTO)).thenReturn(noticesListResponseDTO);
        //when
        NoticesListResponseDTO result = bizEventsService.retrievePaidListFromBizEvents(DUMMY_FISCAL_CODE, noticeRequestDTO);

        //then
        Assertions.assertNotNull(result);
        assertEquals(1, result.getNoticesListDTO().getNotices().size());
        assertEquals(noticesListResponseDTO.getNoticesListDTO().getNotices().get(0), result.getNoticesListDTO().getNotices().get(0));
        assertEquals(CONTINUATION_TOKEN, result.getContinuationToken());

    }

    @Test
    void givenEventIdWhenCallRetrievePaidNoticeDetailsFromBizEventsThenReturnPaidNoticeDetails() {
        //given
        BizEventsPaidNoticeDetailsDTO bizEventsPaidNoticeDetailsDTO = BizEventsPaidNoticeDetailsDTOFaker.mockInstance();
        NoticeDetailsDTO expectedResult = NoticeDetailsDTOFaker.mockInstance();

        Mockito.when(bizEventsPaidNoticeConnectorMock.getPaidNoticeDetails("USER_ID", DUMMY_FISCAL_CODE, "EVENT_ID")).thenReturn(bizEventsPaidNoticeDetailsDTO);
        Mockito.when(bizEventsPaidNoticeDetailsDTO2NoticeDetailsDTOMapperMock.toNoticeDetailsDTO(bizEventsPaidNoticeDetailsDTO)).thenReturn(expectedResult);
        //when
        NoticeDetailsDTO result = bizEventsService.retrievePaidNoticeDetailsFromBizEvents("USER_ID", DUMMY_FISCAL_CODE, "EVENT_ID");

        //then
        Assertions.assertNotNull(result);
        assertEquals(2, result.getCarts().size());
        assertEquals(expectedResult.getInfoNotice(), result.getInfoNotice());
        assertEquals(expectedResult.getCarts(), result.getCarts());
        assertEquals(expectedResult, result);

    }

    @Test
    void givenEventIdWhenCallRetrieveNoticeReceiptFromBizEventsThenReturnNoticeReceipt() throws IOException {
        //given
        Resource receipt = new FileSystemResource("src/test/resources/stub/__files/testReceiptPdfFile.pdf");

        when(bizEventsPaidNoticeConnectorMock.getPaidNoticeReceipt("USER_ID", DUMMY_FISCAL_CODE, "EVENT_ID")).thenReturn(receipt);
        //when
        Resource result = bizEventsService.retrievePaidNoticeReceiptFromBizEvents("USER_ID", DUMMY_FISCAL_CODE, "EVENT_ID");

        //then
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] resultAsByteArray = result.getContentAsByteArray();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(resultAsByteArray, expectedContent);
    }
}