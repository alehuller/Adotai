package br.com.alevhvm.adotai.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // possibilita redirecionamento de dados em cyberataques de um site logado
                                              // para outro

                // gerencia as rotas e os acessos com token e sem
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
                        .permitAll()

                        // Rotas específicas com restrição

                        // Rotas de administrador
                        .requestMatchers("/api/v1/administradores/**").hasRole("ADMINMASTER")


                        // Rotas de usuário
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/nomeUsuario/{nomeUsuario}").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/{nomeUsuario}/adocoes").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/{nomeUsuario}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/{nomeUsuario}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/{nomeUsuario}").hasRole("ADMIN")

                        // Rotas de ong
                        .requestMatchers(HttpMethod.GET, "/api/v1/ongs").hasAnyRole("USER", "ONG")
                        .requestMatchers(HttpMethod.GET, "/api/v1/ongs/nomeUsuario/{nomeUsuario}").hasAnyRole("USER","ONG")
                        .requestMatchers(HttpMethod.GET, "/api/v1/ongs/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/ongs/{id}/adocoes").hasRole("ONG")
                        .requestMatchers(HttpMethod.GET, "/api/v1/ongs/{nomeUsuario}/animais").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/ongs/filtro").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/ongs/{nomeUsuario}").hasRole("ONG")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/ongs/{nomeUsuario}").hasRole("ONG")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/ongs/{nomeUsuario}").hasRole("ONG")

                        // Rotas de animal
                        .requestMatchers(HttpMethod.GET, "/api/v1/animais").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/animais/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/animais/nome/{nome}").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/animais").hasRole("ONG")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/animais/{nome}").hasRole("ONG")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/animais/{nome}").hasRole("ONG")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/animais/{nome}").hasRole("ONG")

                        // Rotas de adoção
                        .requestMatchers(HttpMethod.GET, "/api/v1/adocoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/adocoes/{id}").hasAnyRole("ONG", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/adocoes").hasRole("ONG")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/adocoes/{id}").hasRole("ONG")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/adocoes/{id}").hasRole("ONG")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/adocoes/{id}").hasRole("ONG")

                        // Qualquer outra rota com permissão não especificada fica liberada
                        .anyRequest().permitAll())

                // antes de verificar as roles, vai validar o token do usuário
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // trata as exceções lançadas dentro do filtro
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateful -> guarda no site as infos de user/senha,
                // Stateless -> tokenização n armazena nada no servidor antes de verificar as roles,
                // vai validar o token do usuário
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // ordem dos filtros,
                                                                                             // primeiro parâmetro e dps
                                                                                             // o segundo, q é do spring
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
