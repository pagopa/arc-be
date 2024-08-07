package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.model.generated.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IamUserInfoDTO2UserInfo {

  UserInfo mapIamUserToUserInfo(IamUserInfoDTO userInfoDTO);

}
