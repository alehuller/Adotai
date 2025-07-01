package br.com.alevh.sistema_adocao_pets.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                        .requestMatchers("/api/v1/usuarios/**").hasRole("USER")
                        .requestMatchers("/api/v1/ongs/**").hasRole("ONG")
                        .requestMatchers("/api/v1/**").authenticated()

                        // demais requisições são para usuarios autenticados
                        .anyRequest().permitAll())
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
