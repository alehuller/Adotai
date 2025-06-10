package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.exceptions.InvalidJwtAuthenticationException;
import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// filtro que acontece uma vez em todas as requisições
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        // n tem token, passa mas tbm n autentica nessa porra
        if(token != null){
        var usuario = tokenService.validateToken(token);// valida o token
        UserDetails userDetails = usuarioRepository.findUsuarioByEmail(usuario); //

        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // recupera o token que está presente no header da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null){
            return null;
        }
        // no header ele vem "Bearer token123125135413"
        // desse jeito ele tira esse começo e deixa só o token
        return authHeader.replace("Bearer ", "");
    }

}
