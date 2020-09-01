package com.example.twotokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class ExampleWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().anyRequest().authenticated();

        http.oauth2ResourceServer().authenticationManagerResolver(customAuthenticationManager());
    }

    AuthenticationManagerResolver<HttpServletRequest> customAuthenticationManager() {
        LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers = new LinkedHashMap<>();

        // USE JWT tokens (locally validated) to validate HEAD, GET, and OPTIONS requests
        List<String> readMethod = Arrays.asList("HEAD", "GET", "OPTIONS");
        RequestMatcher readMethodRequestMatcher = request -> readMethod.contains(request.getMethod());
        authenticationManagers.put(readMethodRequestMatcher, jwt());

        // all other requests will use opaque tokens (remotely validated)
        RequestMatchingAuthenticationManagerResolver authenticationManagerResolver
                = new RequestMatchingAuthenticationManagerResolver(authenticationManagers);

        // Use opaque tokens (remotely validated) for all other requests
        authenticationManagerResolver.setDefaultAuthenticationManager(opaque());
        return authenticationManagerResolver;
    }

    AuthenticationManager jwt() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
        return provider::authenticate;
    }

    AuthenticationManager opaque() {
        return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector)::authenticate;
    }
}
