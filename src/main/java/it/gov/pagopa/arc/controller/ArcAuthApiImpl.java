package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ArcAuthApiImpl implements ArcAuthApi {

  @Override
  public ResponseEntity<Void> getAuthenticationEndpoint() {
    return ArcAuthApi.super.getAuthenticationEndpoint();
  }

}