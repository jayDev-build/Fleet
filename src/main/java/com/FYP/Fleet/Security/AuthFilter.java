package com.FYP.Fleet.Security;

import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Models.User;
import com.FYP.Fleet.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class AuthFilter extends OncePerRequestFilter {

    private final AuthUtils authUtils;
    private final UserRepository userRepository;
    @Autowired
    AuthFilter(AuthUtils authUtils, UserRepository userRepository){
        this.authUtils = authUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String headerTokens = request.getHeader("Authorization");

        if(headerTokens == null || !headerTokens.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerTokens.substring(7).trim();
        String userName = authUtils.getUserNameFromToken(token);

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userRepository.findByUsername(userName).orElseThrow(()->
                    new UsernameNotFoundException("UserName Do Not Exists"));
            SecurityUser securityUser = new SecurityUser(user);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    securityUser, null, securityUser.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }

        filterChain.doFilter(request, response);

    }
}
