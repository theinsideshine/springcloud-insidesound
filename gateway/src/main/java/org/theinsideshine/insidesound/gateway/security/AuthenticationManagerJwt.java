package org.theinsideshine.insidesound.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


import static org.theinsideshine.insidesound.gateway.security.TokenJwtConfig.SECRET_KEY;

@Component
public class AuthenticationManagerJwt implements ReactiveAuthenticationManager {


    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication.getCredentials().toString())
                .map(token -> Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody())
                .map(claims -> {
                    String username = claims.get("user_name", String.class);
                    Object authoritiesClaims = claims.get("authorities");
                    Collection<? extends GrantedAuthority> authorities = null;
                    try {
                        authorities = Arrays
                                .asList(new ObjectMapper()
                                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                                        .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return new UsernamePasswordAuthenticationToken(username, null, authorities);

                });
    }
}
