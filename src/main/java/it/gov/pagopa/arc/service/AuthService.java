package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.TokenResponse;
import it.gov.pagopa.arc.model.generated.UserInfo;
import java.util.List;

public interface AuthService {

  UserInfo getUserLoginInfo();

  TokenResponse generateAuthUser();

  List<String> getWhiteListUsers();
}
