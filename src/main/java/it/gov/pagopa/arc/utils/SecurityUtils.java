package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils(){}

  public static IamUserInfoDTO getPrincipal(){
    return (IamUserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
