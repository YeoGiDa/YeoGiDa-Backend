package com.Udemy.YeoGiDa.global.jwt.filter;

import com.Udemy.YeoGiDa.global.jwt.service.JwtProvider;
import com.Udemy.YeoGiDa.global.security.CustomPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomPrincipalDetailsService customPrincipalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtString = request.getHeader("Authorization");
            String jwt = null;
            if(jwtString != null) {
                log.info("jwtString = {}", jwtString);
                jwt = jwtString.split(" ")[1].trim();
                log.info("jwt = {}", jwt);
            }

            if (StringUtils.hasText(jwt) && jwtProvider.validateAccessToken(jwt)) {
                String userEmail = jwtProvider.getEmailFromAccessToken(jwt);

                UserDetails userDetails = customPrincipalDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}