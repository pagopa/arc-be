package it.gov.pagopa.arc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.model.generated.OrganizationsListDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Slf4j
@Service
public class PaymentNoticeSpontaneousServiceImpl implements PaymentNoticeSpontaneousService{

    private final String pathOrganizationsMock;

    private final ObjectMapper objectMapper;

    public PaymentNoticeSpontaneousServiceImpl(@Value("${spontaneous-mock-paths.organizationList}")String pathOrganizationsMock, ObjectMapper objectMapper) {
        this.pathOrganizationsMock = pathOrganizationsMock;
        this.objectMapper = objectMapper;
    }

    @Override
    public OrganizationsListDTO retrieveOrganizations(String userId) {
        OrganizationsListDTO organizationsListDTO = OrganizationsListDTO.builder().organizations(new ArrayList<>()).build();

        log.info("[GET_SPONTANEOUS_ORGANIZATIONS_LIST] User {} initialized spontaneous process and client requested spontaneous organizations list", userId);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pathOrganizationsMock)) {
            if(inputStream == null){
                log.info("File with path [{}] not found", pathOrganizationsMock);
                return organizationsListDTO;
            }
            return objectMapper.readValue(inputStream, OrganizationsListDTO.class);
        } catch (IOException | NullPointerException e) {
            log.info("Error reading the file", e);
            return organizationsListDTO;
        }
    }
}
