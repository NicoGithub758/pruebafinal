package uy.com.provider.hcen_document_provider_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uy.com.provider.hcen_document_provider_api.config.TenantContext;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String tenantIdFromUrl = extractTenantIdFromRequest(request);
        if (tenantIdFromUrl == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("La URL debe contener el ID del tenant. Ej: /mi-tenant/api/...");
            return;
        }

        try {
            TenantContext.setCurrentTenant(tenantIdFromUrl);
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.validateAndGetClaims(token);
            
            String tenantIdFromToken = claims.get("tenant_id", String.class);
            String rol = claims.get("rol", String.class);
            String subject = claims.getSubject();

            if (!tenantIdFromUrl.equals(tenantIdFromToken)) {
                throw new JwtException("Token no válido para el tenant: " + tenantIdFromUrl);
            }
            
            if (!"SYSTEM".equals(rol) || !"hcen_system_service".equals(subject)) {
                 throw new JwtException("El token no pertenece al servicio de sistema de HCEN.");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    subject, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acceso denegado: " + e.getMessage());
        } finally {
            TenantContext.clear();
        }
    }
    
    private String extractTenantIdFromRequest(HttpServletRequest request) {
        String[] parts = request.getRequestURI().split("/");
        return (parts.length > 1) ? parts[1] : null;
    }
}