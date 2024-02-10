package com.joshuapavan.jwtauthentication.configs;

import com.joshuapavan.jwtauthentication.services.JWTService;
import com.joshuapavan.jwtauthentication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationConfig extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if(header.isEmpty() || !StringUtils.startsWithIgnoreCase(header, "Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        final String token = header.substring(7);
        final String email = jwtService.extractUserName(token);

        if(!email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailService().loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                newToken.setDetails(new WebAuthenticationDetailsSource());

                securityContext.setAuthentication(newToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
