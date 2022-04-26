package com.sss.book.springboot.config.auth;

import com.sss.book.springboot.web.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                        .and()
                                .authorizeRequests()
                                        .antMatchers("/","/css/**","/images/**","/js/**","h2-console/**")
                                                .permitAll()
                                        .antMatchers("/api/v1/**")
                                                .hasRole(Role.USER.name())
                                        .anyRequest().authenticated() //나머지 url 은 인증된 사용자(=로그인한 사용자) 에게 허용
                        .and()
                                .logout()
                                        .logoutSuccessUrl("/")
                        .and()
                                .oauth2Login() //oAuth2 로그인 기능에 대한 설정 시작
                                        .userInfoEndpoint()//로그인 성공 후 사용자 정보를 가져올 때 설정
                                                .userService(customOAuth2UserService); //userService 인터페이스 구현제 등록 - 로그인 성공 후 후속 조치
        super.configure(http);
    }
}
