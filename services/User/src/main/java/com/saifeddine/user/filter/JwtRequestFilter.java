package com.saifeddine.user.filter;

import com.saifeddine.user.service.SecurityUserService;
import com.saifeddine.user.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtUtil jwtUtil;
    private final SecurityUserService securityUserService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // List of paths that should be excluded from JWT authentication
    private final List<String> publicPaths = Arrays.asList(
            "/api/auth/**",
            "/api/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, SecurityUserService securityUserService) {
        this.jwtUtil = jwtUtil;
        this.securityUserService = securityUserService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return publicPaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {
            // Extract and process the JWT token
            final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.debug("No Bearer token found in request to {}", request.getRequestURI());
                chain.doFilter(request, response);
                return;
            }

            // Extract the token
            String jwt = authorizationHeader.substring(7);

            // Get username from token
            String username = null;
            try {
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                logger.warn("Invalid JWT token: {}", e.getMessage());
                chain.doFilter(request, response);
                return;
            }

            // Proceed with authentication if username was extracted and no authentication exists
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = securityUserService.loadUserByUsername(username);

                    // Validate token against the specific user
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set authentication in context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Authenticated user: {}, setting security context", username);
                    } else {
                        logger.debug("Token validation failed for user: {}", username);
                    }
                } catch (Exception e) {
                    logger.error("Authentication failed: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        chain.doFilter(request, response);
    }
}