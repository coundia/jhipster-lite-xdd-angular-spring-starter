package com.mycompany.myapp.account.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.shared.authentication.domain.Role;

@IntegrationTest
@AutoConfigureMockMvc
class AccountResourceIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
  void shouldGetAuthenticatedUserAccount() throws Exception {
    mockMvc
      .perform(get("/api/account"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.login").value("user"))
      .andExpect(jsonPath("$.authorities").value(Role.USER.key()));
  }

  @Test
  @WithAnonymousUser
  void shouldNotGetAccountForNotAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/api/account")).andExpect(status().isForbidden());
  }
}
