package com.mycompany.myapp.wire.springdoc.infrastructure.primary;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mycompany.myapp.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@Configuration
@AutoConfigureBefore(SpringdocConfiguration.class)
@ExcludeFromGeneratedCodeCoverage(reason = "Not called by integration tests")
class SpringdocJWTConfiguration {

  private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

  @Bean
  GlobalOpenApiCustomizer jwtOpenApi() {
    return openApi ->
      openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)).components(jwtComponents(openApi.getComponents()));
  }

  private Components jwtComponents(Components existingComponents) {
    return existingComponents.addSecuritySchemes(
      SECURITY_SCHEME_NAME,
      new SecurityScheme().name(SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
    );
  }
}
