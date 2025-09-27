package br.com.alevhvm.adotai.auth.security;

import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.auth.repository.LoginIdentityViewRepository;
import br.com.alevhvm.adotai.auth.service.TokenBlackListService;
import br.com.alevhvm.adotai.auth.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final LoginIdentityViewRepository loginIdentityViewRepository;
    private final TokenBlackListService tokenBlackListService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private static final List<String> ROTAS_PUBLICAS = List.of(
            "/auth/user/login",
            "/auth/user/register",
            "/auth/ong/login",
            "/auth/ong/register",
            "/auth/admin/login",
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
            "/webjars/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getServletPath();

        if (!shouldNotFilter(request)) {
            var token = this.recoverToken(request);
            if (token != null) {
                if(tokenBlackListService.isTokenBlacklisted(token)){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException("usuário fez logout") {
                    });
                    return;
                }
                try {
                    var email = tokenService.validateToken(token);

                    Optional<LoginIdentityView> userOptional = loginIdentityViewRepository.findByEmail(email);
                    if(userOptional.isEmpty()){
                        jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException("Usuário não encontrado: " + email){});
                        return;
                    } else{
                        UserDetails userDetails = userOptional.get();
                        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (JWTVerificationException ex) {
                    jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException("Token JWT inválido ou expirado", ex) {
                    });
                    return;
                }
                catch (AuthenticationCredentialsNotFoundException ex) {
                    jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException("Acesso negado: nenhuma credencial informada", ex) {
                    });
                    return;
                }
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
        String path = request.getServletPath();
        return ROTAS_PUBLICAS.stream().anyMatch(path::startsWith);
    }
}
