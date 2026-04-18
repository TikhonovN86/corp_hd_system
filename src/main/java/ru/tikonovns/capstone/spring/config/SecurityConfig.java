package ru.tikonovns.capstone.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.tikonovns.capstone.spring.security.AppUserDetailsService;

/**
 * Класс-точка настройки Spring Security.
 * Отвечает за то, как проходит логин, как проверяется пароль,
 * как создается сессия, кто имеет доступ к каким эндпоинтам.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;

    /*
     Класс - основная конфигурация безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 CSRF — защита от поддельных запросов.
                 Для первого этапа и тестовых endpoint'ов отключил CSRF.
                 Когда появятся реальные формы/изменяющие POST-запросы, включу обратно.
                 */
                .csrf(csrf -> csrf.disable())

                //Авторизация (доступ к эндпоинтам)
                .authorizeHttpRequests(auth -> auth
                        //Эти URL доступны без логина
                        .requestMatchers(
                                "/login",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        //Ограничение по ролям. Spring берет principal.getAuthorities() и проверяет наличие роли.
                        .requestMatchers("/api/test/initiator/**").hasAuthority("ROLE_INITIATOR")
                        .requestMatchers("/api/test/dispatcher/**").hasAuthority("ROLE_DISPATCHER")
                        .requestMatchers("/api/test/executor/**").hasAuthority("ROLE_EXECUTOR")

                        //Все остальное только для авторизованных.
                        .anyRequest().authenticated()
                )

                //Form Login
                .formLogin(form -> form
                        .defaultSuccessUrl("/api/test/me", true)
                )

                //Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        //Очистка сессии
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .sessionManagement(session -> session
                        //Сессия создается только при логине.
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                        //Ограничение сессий.
                        .maximumSessions(1)
                )

                .authenticationProvider(authenticationProvider());

                //Позволяет логиниться через Basic.
//                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /*
    Класс, который использует UserDetailsService (мой AppUserDetailsService)
    и использует PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //Где брать пользователя
        provider.setUserDetailsService(appUserDetailsService);
        //Как проверять пароль.
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //Введённый пароль -> bcrypt -> сравнение с hash
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}