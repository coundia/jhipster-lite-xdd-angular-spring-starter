package com.mycompany.myapp.account.infrastructure.primary;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Set;
import com.mycompany.myapp.shared.error.domain.Assert;

@Schema(name = "account", description = "Information for the authenticated user account")
class RestAccount {

  private final String login;
  private final Set<String> authorities;

  public RestAccount(String login, Set<String> authorities) {
    Assert.notBlank("login", login);

    this.login = login;
    this.authorities = authorities;
  }

  @Schema(description = "Login of the authenticated user", requiredMode = RequiredMode.REQUIRED)
  public String getLogin() {
    return login;
  }

  @Schema(description = "Authorities of the authenticated user")
  public Set<String> getAuthorities() {
    return authorities;
  }
}
