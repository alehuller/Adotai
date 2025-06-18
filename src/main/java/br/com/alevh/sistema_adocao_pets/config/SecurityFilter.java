package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.repository.LoginIdentityViewRepository;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenBlackListService;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenService;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        token = null;
        // n tem token, passa mas tbm n autentica nessa porra
        if (token != null) {

            if(tokenBlackListService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido: usuário fez logout");
                return;
            }

            var email = tokenService.validateToken(token);// valida o token
            UserDetails userDetails = loginIdentityViewRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email)); //

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // recupera o token que está presente no header da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization").replace("Bearer ", "");
        // no header ele vem "Bearer token123125135413"
        // desse jeito ele tira esse começo e deixa só o token
        return authHeader;
    }

}
