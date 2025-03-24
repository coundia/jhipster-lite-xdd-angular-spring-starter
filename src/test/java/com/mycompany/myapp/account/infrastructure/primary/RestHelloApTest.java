package com.mycompany.myapp.account.infrastructure.primary;

import com.mycompany.myapp.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class RestHelloApTest {

  @Autowired
  private MockMvc mockMvc;
  @Test
   void testHelloSecured() throws Exception {

    mockMvc
      .perform(get("/hello"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Hello World"))

      ;

  }
}
