package pl.mrozek.inzynierka.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailServiceB userDetailServiceB;

    public SpringSecurityConfig(UserDetailServiceB userDetailServiceB) {
        this.userDetailServiceB = userDetailServiceB;
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.cors().and().csrf().disable();
//http.authorizeRequests().anyRequest().permitAll();
//    }
//
//    @Bean
//    public ErrorPageFilter errorPageFilter() {
//        return new ErrorPageFilter();
//    }
//
//    @Bean
//    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(filter);
//        filterRegistrationBean.setEnabled(false);
//        return filterRegistrationBean;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);


//        User user = new User("admin",passwordEncoder().encode("admin"),
//                Collections.singleton( new SimpleGrantedAuthority("ROLE_ADMIN")));
//        User user1 = new User("user",passwordEncoder().encode("user"),
//                Collections.singleton( new SimpleGrantedAuthority("ROLE_USER")));
//
//        auth.inMemoryAuthentication().withUser(user);
//        auth.inMemoryAuthentication().withUser(user1);
//        auth.userDetailsService()
        auth.userDetailsService(userDetailServiceB);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.authorizeRequests().anyRequest().permitAll();

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST).permitAll()
                .antMatchers("/struktura").hasRole("ADMIN")
                .antMatchers("/skladniki").hasAnyRole("ADMIN","USER")
                .and()
                .formLogin().permitAll();

//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll();
    }


}
