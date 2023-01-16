package com.example.springsecuritystudy202301.common.config.security;

import com.example.springsecuritystudy202301.common.jwt.JwtAccessDeniedHandler;
import com.example.springsecuritystudy202301.common.jwt.JwtAuthenticationEntryPoint;
import com.example.springsecuritystudy202301.common.jwt.JwtFilter;
import com.example.springsecuritystudy202301.common.jwt.JwtSecurityConfig;
import com.example.springsecuritystudy202301.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SpringSecurityConfig {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic().disable(); // basic auth 비활성화

        http
                .csrf().disable() // 토큰방식 인증을 사용하므로 Cross-Site Request Forgery protection CSRF 보안 비활성화
                // exception 핸들러 세팅
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 세션을 사용하지 않으므로 세션 비활성화
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 api 는 토큰이 없는 상태에서 요청하기 떄문에 permitAll 설정=
                .and()
                .authorizeRequests()
                .antMatchers("/test/signup").permitAll()
                .antMatchers("/test/signin").permitAll()
                .antMatchers("/swh/api/session/**").permitAll()

                // 스웨거
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
//                .anyRequest().permitAll()
                .antMatchers("/test/admin").hasRole("ADMIN")
                .anyRequest().authenticated()

                // jwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))

                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
