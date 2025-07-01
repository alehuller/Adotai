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

@Component
@RequiredArgsConstructor
// filtro que acontece uma vez em todas as requisições
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final LoginIdentityViewRepository loginIdentityViewRepository;

    private final TokenBlackListService tokenBlackListService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(!shouldNotFilter(request)){
            var token = this.recoverToken(request);
            // n tem token, passa mas tbm n autentica nessa porra
            if (token != null) {
                if (tokenBlackListService.isTokenBlacklisted(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token inválido: usuário fez logout");
                    return;
                }
                try {
                    var email = tokenService.validateToken(token);// valida o token
                    UserDetails userDetails = loginIdentityViewRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email)); //

                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException ex) {
                    jwtAuthenticationEntryPoint.commence(request, response, new TokenInvalidException("Token JWT inválido ou expirado", ex)); // <-- aqui está a mágica
                }
            }
        }

    }

    // recupera o token que está presente no header da requisição
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
        return path.startsWith("/auth");
    }
}
