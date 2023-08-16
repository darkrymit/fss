package ua.edu.nau.darkrymit.fss.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      Converter<Jwt, AbstractAuthenticationToken> converter, JwtDecoder decoder) throws Exception {
// @formatter:off
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
//            auth -> auth.anyRequest().authenticated()
            auth -> auth.anyRequest().permitAll()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(r -> r.jwt().decoder(decoder).jwtAuthenticationConverter(converter))
        .build();
// @formatter:on
  }

  @Bean
  @SuppressWarnings("unchecked")
  Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationTokenConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Iterable<String> roles = (Iterable<String>) jwt.getClaimAsMap("realm_access").get("roles");
      return StreamSupport.stream(roles.spliterator(), false)
          .map(s -> new SimpleGrantedAuthority("ROLE_" + s)).map(GrantedAuthority.class::cast)
          .toList();
    });
    return converter;
  }
}
