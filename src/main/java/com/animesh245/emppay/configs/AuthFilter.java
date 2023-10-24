package com.animesh245.emppay.configs;

import com.animesh245.emppay.services.AuthService;
import com.animesh245.emppay.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter
{

    //get jwt
    //Bearer
    //validate
    private final UserService userService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // checking null and format
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
        {
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = authService.extractUsername(jwtToken);
                System.out.println("Username extracted from token " + username);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            UserDetails userDetails = userService.loadUserByUsername(username);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else
            {
                System.out.println("Token is not validated..");
            }
        }
        filterChain.doFilter(request, response);
    }
}

