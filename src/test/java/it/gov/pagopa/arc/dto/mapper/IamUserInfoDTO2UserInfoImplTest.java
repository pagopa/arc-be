package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

class IamUserInfoDTO2UserInfoImplTest {

  @InjectMocks
  IamUserInfoDTO2UserInfo mapper = Mappers.getMapper(IamUserInfoDTO2UserInfo.class);

  @Test
  void givenIamUserInfoDTOThenMapIt2UserInfo(){
    Assertions.assertNull(mapper.mapIamUserToUserInfo(null));

    UserInfo user = mapper.mapIamUserToUserInfo(IamUserInfoDTOFaker.mockInstance());
    Assertions.assertNotNull(user);
    Assertions.assertEquals(user.getUserId(),IamUserInfoDTOFaker.mockInstance().getUserId());
    Assertions.assertEquals(user.getName(),IamUserInfoDTOFaker.mockInstance().getName());
    Assertions.assertEquals(user.getFamilyName(),IamUserInfoDTOFaker.mockInstance().getFamilyName());
    Assertions.assertEquals(user.getEmail(),IamUserInfoDTOFaker.mockInstance().getEmail());
    Assertions.assertEquals(user.getFiscalCode(),IamUserInfoDTOFaker.mockInstance().getFiscalCode());

  }

}