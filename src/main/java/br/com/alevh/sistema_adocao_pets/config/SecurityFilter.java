package br.com.alevh.sistema_adocao_pets.config;

import java.io.IOException;

import br.com.alevh.sistema_adocao_pets.repository.LoginIdentityRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// filtro que acontece uma vez em todas as requisições
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final LoginIdentityRepository loginIdentityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);

        // n tem token, passa mas tbm n autentica nessa porra
        if (token != null) {
            var email = tokenService.validateToken(token);// valida o token
            UserDetails userDetails = loginIdentityRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email)); //

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // recupera o token que está presente no header da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        // no header ele vem "Bearer token123125135413"
        // desse jeito ele tira esse começo e deixa só o token
        return authHeader.replace("Bearer ", "");
    }

}
