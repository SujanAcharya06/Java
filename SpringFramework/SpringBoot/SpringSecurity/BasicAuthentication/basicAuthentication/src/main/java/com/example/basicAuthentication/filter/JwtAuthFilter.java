package com.example.basicAuthentication.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.basicAuthentication.service.CustomUserDetailsService;
import com.example.basicAuthentication.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    // OncePerRequestFilter -> a filter which comes into picture only once per each request

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Todo fetch user by username
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            // Todo validate token
            //
            if(jwtUtil.validateToken(username, userDetails, token)){
                // userDetails.getAuthorities() returns the role
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // we want to set all HttpServletRequest request (details)to authToken
                // because this authToken from above will have only will have usserDetails , password and authority details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Todo set to spring context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // System.out.println("Auth Header: " + authHeader);
            // System.out.println("Extracted username: " + username);
            // System.out.println("Is token valid: " + jwtUtil.validateToken(username, userDetails, token));
        }
        filterChain.doFilter(request, response);

    }


}
