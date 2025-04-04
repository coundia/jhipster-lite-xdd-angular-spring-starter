package com.mycompany.myapp.account.infrastructure.secondary;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Repository;
import com.mycompany.myapp.account.domain.AuthenticationQuery;
import com.mycompany.myapp.account.domain.Token;
import com.mycompany.myapp.account.domain.TokensRepository;
import com.mycompany.myapp.shared.authentication.domain.Role;
import com.mycompany.myapp.shared.error.domain.Assert;

@Repository
class JwtTokensRepository implements TokensRepository {

  private final SecretKey key;
  private final long validityDuration;
  private final long rememberMeValidityDuration;

  public JwtTokensRepository(JwtTokensProperties properties) {
    key = Keys.hmacShaKeyFor(properties.getJwtBase64Secret().getBytes(StandardCharsets.UTF_8));
    validityDuration = properties.getTokenValidity().toSeconds();
    rememberMeValidityDuration = properties.getRememberMeTokenValidity().toSeconds();
  }

  public Token buildToken(AuthenticationQuery query) {
    Assert.notNull("query", query);

    String token = Jwts.builder()
      .subject(query.username().get())
      .claim("auth", buildAuthorities(query))
      .signWith(key, Jwts.SIG.HS512)
      .expiration(getValidity(query))
      .compact();

    return new Token(token);
  }

  private String buildAuthorities(AuthenticationQuery query) {
    return query.roles().stream().map(Role::key).collect(Collectors.joining(","));
  }

  private Date getValidity(AuthenticationQuery query) {
    if (query.rememberMe()) {
      return Date.from(Instant.now().plusSeconds(rememberMeValidityDuration));
    }

    return Date.from(Instant.now().plusSeconds(validityDuration));
  }
}
