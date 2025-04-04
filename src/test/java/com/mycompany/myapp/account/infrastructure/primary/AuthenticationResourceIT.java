package com.mycompany.myapp.account.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.mycompany.myapp.IntegrationTest;

@IntegrationTest
@AutoConfigureMockMvc
class AuthenticationResourceIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAuthorizeKnownAccount() throws Exception {
    String query = authenticationBuilder().build();

    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(query))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id_token").isString())
      .andExpect(jsonPath("$.id_token").isNotEmpty())
      .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  void shouldAuthorizeRememberedWithRememberMe() throws Exception {
    String query = authenticationBuilder().rememberMe(true).build();

    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(query))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id_token").isString())
      .andExpect(jsonPath("$.id_token").isNotEmpty())
      .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  void shouldNotAuthorizeAccountWithBadPassword() throws Exception {
    String query = authenticationBuilder().password("dummy").build();

    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(query))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.id_token").doesNotExist())
      .andExpect(header().doesNotExist("Authorization"));
  }

  @Test
  void shouldNotAuthorizeUnknownAccount() throws Exception {
    String query = authenticationBuilder().username("unknown").build();

    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(query))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.id_token").doesNotExist())
      .andExpect(header().doesNotExist("Authorization"));
  }

  @Test
  @WithMockUser
  void shouldGetAuthenticatedUserUsername() throws Exception {
    mockMvc.perform(get("/api/authenticate")).andExpect(status().isOk()).andExpect(content().string(containsString("user")));
  }

  private static TestAuthenticationQueryBuilder authenticationBuilder() {
    return new TestAuthenticationQueryBuilder();
  }

  private static final class TestAuthenticationQueryBuilder {

    private static final String AUTHENTICATION_TEMPLATE =
      """
      {
        "username": "{USER}",
        "password": "{PASSWORD}",
        "rememberMe": {REMEMBER_ME}
      }
      """;

    private String username = "admin";
    private String password = "admin";
    private boolean rememberMe;

    public TestAuthenticationQueryBuilder username(String username) {
      this.username = username;

      return this;
    }

    public TestAuthenticationQueryBuilder password(String password) {
      this.password = password;

      return this;
    }

    public TestAuthenticationQueryBuilder rememberMe(boolean rememberMe) {
      this.rememberMe = rememberMe;

      return this;
    }

    public String build() {
      return AUTHENTICATION_TEMPLATE.replace("{USER}", username)
        .replace("{PASSWORD}", password)
        .replace("{REMEMBER_ME}", String.valueOf(rememberMe));
    }
  }
}
