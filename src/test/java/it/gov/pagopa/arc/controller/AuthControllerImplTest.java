package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcAuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(value = {
    ArcAuthApi.class
})
class AuthControllerImplTest {

  @Autowired
  private MockMvc mockMvc;

}