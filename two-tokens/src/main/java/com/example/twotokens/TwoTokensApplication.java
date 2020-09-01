package com.example.twotokens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class TwoTokensApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoTokensApplication.class, args);
    }

    @Bean
    JwtDecoder oktaJwtDecoder(OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {

        String issuer = oAuth2ResourceServerProperties.getJwt().getIssuerUri();
        String jwkSetUri = issuer + "/v1/keys";

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        // default validators
        validators.add(new JwtTimestampValidator());
        validators.add(new JwtIssuerValidator(issuer));

        // okta recommends also validating the `aud` claim
        // see: https://developer.okta.com/docs/guides/validate-access-tokens/java/overview/
        validators.add(token -> {
            Set<String> expectedAudience = new HashSet<>();
            // Add validation of the audience claim
            expectedAudience.add("api://default");
            // For new Okta orgs, the default audience is `api://default`,
            // if you have changed this from the default update this value
            return !Collections.disjoint(token.getAudience(), expectedAudience)
                    ? OAuth2TokenValidatorResult.success()
                    : OAuth2TokenValidatorResult.failure(new OAuth2Error(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "This aud claim is not equal to the configured audience",
                    "https://tools.ietf.org/html/rfc6750#section-3.1"));
        });
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(validators);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
