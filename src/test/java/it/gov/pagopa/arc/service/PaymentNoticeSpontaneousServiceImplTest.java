package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.model.generated.OrganizationDTO;
import it.gov.pagopa.arc.model.generated.OrganizationsListDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentNoticeSpontaneousServiceImplTest {

    private static final String USER_ID = "user_id";

    private ObjectMapper objectMapper;

    PaymentNoticeSpontaneousService spontaneousService;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.objectMapper;
    }

    @Test
    void givenRequestWhenRetrieveOrganizationsThenReturnOrganizationsList() {
        //given
        spontaneousService = new PaymentNoticeSpontaneousServiceImpl("mock/organizationsMock.json",objectMapper);
        //when
        OrganizationsListDTO result = spontaneousService.retrieveOrganizations(USER_ID);
        //then
        assertNotNull(result);
        assertEquals(6, result.getOrganizations().size());

        OrganizationDTO firstOrganizationDTO = result.getOrganizations().getFirst();
        assertEquals("Comune di Milano", firstOrganizationDTO.getPaFullName());
        assertEquals("80237060158", firstOrganizationDTO.getPaTaxCode());
        assertEquals("C_I17021", firstOrganizationDTO.getIpaCode());

        OrganizationDTO secondOrganizationDTO = result.getOrganizations().get(1);
        assertEquals("INPS - Istituto Nazionale della Previdenza Sociale", secondOrganizationDTO.getPaFullName());
        assertEquals("80078720587", secondOrganizationDTO.getPaTaxCode());
        assertEquals("G270", secondOrganizationDTO.getIpaCode());

        OrganizationDTO thirdOrganizationDTO = result.getOrganizations().get(2);
        assertEquals("Ministero dell'Interno", thirdOrganizationDTO.getPaFullName());
        assertEquals("80012070584", thirdOrganizationDTO.getPaTaxCode());
        assertEquals("M_ING", thirdOrganizationDTO.getIpaCode());

        OrganizationDTO fourthOrganizationDTO = result.getOrganizations().get(3);
        assertEquals("Comune di Roma", fourthOrganizationDTO.getPaFullName());
        assertEquals("80204350588", fourthOrganizationDTO.getPaTaxCode());
        assertEquals("C_I16892", fourthOrganizationDTO.getIpaCode());

        OrganizationDTO fifthOrganizationDTO = result.getOrganizations().get(4);
        assertEquals("Agenzia delle Entrate", fifthOrganizationDTO.getPaFullName());
        assertEquals("97052740589", fifthOrganizationDTO.getPaTaxCode());
        assertEquals("A010", fifthOrganizationDTO.getIpaCode());

        OrganizationDTO sixthOrganizationDTO = result.getOrganizations().get(5);
        assertEquals("Ente di Test", sixthOrganizationDTO.getPaFullName());
        assertEquals("99999999982", sixthOrganizationDTO.getPaTaxCode());
        assertEquals("TEST_01", sixthOrganizationDTO.getIpaCode());
    }

    @Test
    void givenMockedPathWhenRetrieveOrganizationsThenReturnExpectedResult() {
        //given
        spontaneousService = new PaymentNoticeSpontaneousServiceImpl("wrong/path",objectMapper);
        //when
        OrganizationsListDTO result = spontaneousService.retrieveOrganizations(USER_ID);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getOrganizations().size());

    }

    @Test
    void givenNullPathWhenRetrieveOrganizationsThenReturnExpectedResult() {
        //given
        spontaneousService = new PaymentNoticeSpontaneousServiceImpl(null,objectMapper);
        //when
        OrganizationsListDTO result = spontaneousService.retrieveOrganizations(USER_ID);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getOrganizations().size());

    }

}