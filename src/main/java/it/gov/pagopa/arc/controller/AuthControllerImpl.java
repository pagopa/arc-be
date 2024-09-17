package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;
import it.gov.pagopa.arc.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AuthControllerImpl implements ArcAuthApi {

  private final AuthService authService;

  public AuthControllerImpl(AuthService authService){
    this.authService = authService;
  }

  @Override
  public ResponseEntity<UserInfo> getUserInfo() {
    return new ResponseEntity<>(authService.getUserLoginInfo(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<TokenResponse> getTestUserInfo() {
    return new ResponseEntity<>(authService.getTestUserLoginInfo(), HttpStatus.OK);
  }

}
