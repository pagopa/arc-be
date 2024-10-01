package it.gov.pagopa.arc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.controller.generated.ArcNoticesApi;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.security.JwtAuthenticationFilter;
import it.gov.pagopa.arc.service.NoticesService;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {
        ArcNoticesApi.class
},excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class NoticesControllerImplTest {
    private static final int SIZE = 2;
    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String ORDER_BY = "TRANSACTION_DATE";
    private static final String ORDERING = "DESC";


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoticesService noticesServiceMock;

    @Test
    void givenFiscalCodeWhenCallGetNoticesListThenReturnNoticesList() throws Exception {
        //Given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(List.of(noticeDTO)).build();
        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder()
                .noticesListDTO(noticesListDTO)
                .continuationToken(CONTINUATION_TOKEN).build();

        Mockito.when(noticesServiceMock.retrieveNoticesAndToken(CONTINUATION_TOKEN,SIZE,true, true, ORDER_BY ,ORDERING )).thenReturn(noticesListResponseDTO);

        //When
        MvcResult result = mockMvc.perform(
                        get("/notices")
                                .header("x-continuation-token", CONTINUATION_TOKEN)
                                .param("size", String.valueOf(SIZE))
                                .param("paidByMe", String.valueOf(true))
                                .param("registeredToMe", String.valueOf(true))
                                .param("orderBy", ORDER_BY)
                                .param("ordering", ORDERING)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(header().string("x-continuation-token", CONTINUATION_TOKEN))
                .andReturn();

        NoticesListDTO resultResponse = TestUtils.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                NoticesListDTO.class);


        //Then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(noticesListDTO, resultResponse);
        Mockito.verify(noticesServiceMock).retrieveNoticesAndToken(anyString(),anyInt(),anyBoolean(),anyBoolean(),anyString(),anyString());
    }

    @Test
    void givenFiscalCodeWhenCallGetNoticesListThenReturnNoticesListWithNullContinuationToken() throws Exception {
        //Given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(2, false);
        noticeDTO.setRegisteredToMe(false);
        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(List.of(noticeDTO)).build();
        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder()
                .noticesListDTO(noticesListDTO)
                .continuationToken(null).build();

        Mockito.when(noticesServiceMock.retrieveNoticesAndToken(CONTINUATION_TOKEN,SIZE,true, false, ORDER_BY ,ORDERING )).thenReturn(noticesListResponseDTO);

        //When
        MvcResult result = mockMvc.perform(
                        get("/notices")
                                .header("x-continuation-token", CONTINUATION_TOKEN)
                                .param("size", String.valueOf(SIZE))
                                .param("paidByMe", String.valueOf(true))
                                .param("registeredToMe", String.valueOf(false))
                                .param("orderBy", ORDER_BY)
                                .param("ordering", ORDERING)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(header().string("x-continuation-token", nullValue()))
                .andReturn();

        NoticesListDTO resultResponse = TestUtils.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                NoticesListDTO.class);


        //Then
        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals(noticesListDTO, resultResponse);
        Mockito.verify(noticesServiceMock).retrieveNoticesAndToken(anyString(),anyInt(),anyBoolean(),anyBoolean(),anyString(),anyString());
    }
}