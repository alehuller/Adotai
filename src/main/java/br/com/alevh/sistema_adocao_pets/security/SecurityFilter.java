package br.com.alevh.sistema_adocao_pets.security;

import br.com.alevh.sistema_adocao_pets.exceptions.TokenInvalidException;
import br.com.alevh.sistema_adocao_pets.repository.LoginIdentityViewRepository;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenBlackListService;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final LoginIdentityViewRepository loginIdentityViewRepository;
    private final TokenBlackListService tokenBlackListService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final List<String> ROTAS_PUBLICAS = List.of(
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/swagger-config",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-resources",
            "/swagger-resources/",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/",
            "/webjars/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Ignora rotas públicas (Swagger, webjars, auth, etc.)
        if (ROTAS_PUBLICAS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        if (token != null) {
            if (tokenBlackListService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido: usuário fez logout");
                return;
            }
            try {
                var email = tokenService.validateToken(token);
                UserDetails userDetails = loginIdentityViewRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException ex) {
                jwtAuthenticationEntryPoint.commence(
                        request, response,
                        new TokenInvalidException("Token JWT inválido ou expirado", ex)
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.equals("Bearer null")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false; // usando ROTAS_PUBLICAS, então esse método pode sempre retornar false
    }
}
