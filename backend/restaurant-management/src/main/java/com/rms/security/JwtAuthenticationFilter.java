package com.rms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Check: Filter call ho raha hai ya nahi
        System.out.println("🔥 JWT FILTER CALLED");

        // 2️⃣ Authorization header read karo
        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER: " + authHeader);

        // 3️⃣ Agar token nahi hai
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 4️⃣ Bearer ke baad actual token nikalo
        String token = authHeader.substring(7);

        // 5️⃣ Token se email aur role extract karo
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        // 6️⃣ Token validate karo
        boolean tokenValid =
                jwtUtil.validateToken(token, email);

        System.out.println("EMAIL: " + email);
        System.out.println("ROLE: " + role);
        System.out.println("TOKEN VALID: " + tokenValid);

        // 7️⃣ Authentication create karo
        if (email != null &&
                tokenValid &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            // ADMIN → ROLE_ADMIN
            // CUSTOMER → ROLE_CUSTOMER
            String authority = "ROLE_" + role;

            System.out.println(
                    "AUTHORITY: " + authority
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            authority
                                    )
                            )
                    );

            // 8️⃣ SecurityContext me authentication save karo
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            System.out.println(
                    "✅ AUTHENTICATION SET SUCCESSFULLY"
            );
        }

        // 9️⃣ Request ko next filter/controller par bhejo
        filterChain.doFilter(request, response);
    }
}