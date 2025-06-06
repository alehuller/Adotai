package br.com.alevh.sistema_adocao_pets.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return  httpSecurity
                .csrf(csrf -> csrf.disable()) // possibilita redirecionamento de dados em cyberataques de um site logado para outro
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateful -> guarda no site as infos de user/senha, Stateless -> tokenização, n armazena nada no servidor
                // gerencia as rotas e os acessos com token e sem
                .authorizeHttpRequests(authorize -> authorize

                        // permite que todos disparem requisições de login e de registro
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // permite que apenas admins adicionem novos produtos
                        .requestMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")

                        // demais requisições são para usuarios autenticados
                        .anyRequest().authenticated()
                )

                // antes de verificar as roles, vai validar o token do usuário
                .addFilterBefore(securityFilter,  UsernamePasswordAuthenticationFilter.class) // ordem dos filtros, primeiro parâmetro e dps o segundo, q é do spring
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
