package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticeSpontaneousApi;
import it.gov.pagopa.arc.model.generated.OrganizationsListDTO;
import it.gov.pagopa.arc.service.PaymentNoticeSpontaneousService;
import it.gov.pagopa.arc.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentNoticeSpontaneousControllerImpl implements ArcPaymentNoticeSpontaneousApi {
    private final PaymentNoticeSpontaneousService paymentNoticeSpontaneousService;

    public PaymentNoticeSpontaneousControllerImpl(PaymentNoticeSpontaneousService paymentNoticeSpontaneousService) {
        this.paymentNoticeSpontaneousService = paymentNoticeSpontaneousService;
    }

    @Override
    public ResponseEntity<OrganizationsListDTO> getOrganizations() {
        String userId = SecurityUtils.getUserId();
        OrganizationsListDTO organizationsListDTO = paymentNoticeSpontaneousService.retrieveOrganizations(userId);

        return ResponseEntity.ok().body(organizationsListDTO);
    }

}
