package be.ucll.se.team15backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    private final AuthenticationProvider authenticationProvider;
    private final JwtAthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //volgorde is belangrijk!!!!!!!
                                .requestMatchers("/rentals").permitAll()
                                .requestMatchers("/rentals/*" ,"/rentals/add/*").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/complaints").hasAnyAuthority("ADMIN")
                                .requestMatchers("/complaints/submit").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/rentals/remove/*").hasAnyAuthority("OWNER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/planner", "/map/**").permitAll()
                                .requestMatchers("/cars", "/cars/**").hasAnyAuthority("OWNER", "ADMIN")
                                .requestMatchers("/rents/add/*", "/rents/remove/*").hasAnyAuthority("OWNER", "ADMIN")
                                .requestMatchers("/rents", "/rents/**").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/chat/**").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/chat/send").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN", "BOT")
                                .requestMatchers("/notifications/**").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN", "BOT")
                                .requestMatchers("/bot").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/billing/**").hasAnyAuthority("OWNER", "RENTER", "ACCOUNTANT", "ADMIN")
                                .requestMatchers("/manage/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("/", "/auth/**", "/*", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console", "/h2-console/**").permitAll()
                                .requestMatchers("/**").hasAnyAuthority("ADMIN")

                                .anyRequest().denyAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


//    @Bean
//    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher((matchers) -> matchers.request
//                .cors()
//                .and()
//                .csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/*", "/auth/**")
//                .permitAll()
//                .requestMatchers("/api/**")
//                .hasAnyAuthority("KLANT", "MEDEWERKER", "ADMIN")
//                .requestMatchers("/ms/**")
//                .hasAnyAuthority("MEDEWERKER", "ADMIN")
//                .requestMatchers("/ks/**")
//                .hasAnyAuthority("KLANT", "ADMIN")
//                .requestMatchers("/**")
//                .hasAnyAuthority("ADMIN")
//                .anyRequest()
//                .authenticated()
//                .and()
//
//                .exceptionHandling()
//                .accessDeniedPage("/denied")
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}