package dev.enricosola.yummy.config;

import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import dev.enricosola.yummy.service.InternalUserDetailsService;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String REMEMEBER_ME_KEY = "remember_me";

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new InternalUserDetailsService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository persistentTokenRepository) throws Exception {
        http.userDetailsService(this.userDetailsService());
        http.authorizeHttpRequests((auth) -> {
            auth.requestMatchers("/public/table/**").permitAll();
            auth.requestMatchers("/resources/**").permitAll();
            auth.requestMatchers("/storage/**").permitAll();
            auth.requestMatchers("/login").permitAll();
            auth.requestMatchers("/").permitAll();
            auth.anyRequest().authenticated();
        }).formLogin((formLogin) -> {
            formLogin.loginPage("/login").permitAll();
            formLogin.defaultSuccessUrl("/dashboard", true);
        }).rememberMe((remember) -> {
            remember.rememberMeServices(new PersistentTokenBasedRememberMeServices(
                SecurityConfig.REMEMEBER_ME_KEY,
                this.userDetailsService(),
                persistentTokenRepository
            ));
        });
        return http.build();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall(){
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowUrlEncodedSlash(true);
        strictHttpFirewall.setAllowSemicolon(true);
        return strictHttpFirewall;
    }
}
