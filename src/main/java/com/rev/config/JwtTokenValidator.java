package com.rev.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;


public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Only process the JWT if the Authorization header is valid
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);

            // Make sure a token actually exists
            if (!jwt.isBlank()) {
                try {
                    SecretKey key = Keys.hmacShaKeyFor(
                            JWT_CONSTANT.SECRET_KEY.getBytes()
                    );

                    Claims claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(jwt)
                            .getPayload();

                    String email = String.valueOf(claims.get("email"));
                    String authorities = String.valueOf(claims.get("authorities"));

                    List<GrantedAuthority> auths =
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    auths
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid JWT token...");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
