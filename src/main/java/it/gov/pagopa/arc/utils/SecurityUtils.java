package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils(){}

  public static IamUserInfoDTO getPrincipal() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    try{
      return (IamUserInfoDTO) principal;
    }catch (ClassCastException e){
      throw new IllegalStateException("Invalid principal type: expected IamUserInfoDTO but got " + principal.getClass().getName());
    }

  }

  public static String getUserFiscalCode() {
    IamUserInfoDTO principal = getPrincipal();

    if (principal.getFiscalCode() == null) {
      throw new IllegalArgumentException("Fiscal code is missing for the authenticated user");
    }

    return principal.getFiscalCode();
  }


}
