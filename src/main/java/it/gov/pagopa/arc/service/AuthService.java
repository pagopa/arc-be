package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;

public interface AuthService {

  UserInfo getUserLoginInfo();

  TokenResponse getTestUserLoginInfo();
}
