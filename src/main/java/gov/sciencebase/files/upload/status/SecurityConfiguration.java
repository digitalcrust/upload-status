package gov.sciencebase.files.upload.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.oauth2.client.OAuth2ClientContext;
// import org.springframework.security.oauth2.client.OAuth2RestTemplate;
// import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
// import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
// import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.filter.CompositeFilter;

//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import java.security.Principal;

@Configuration
// @EnableOAuth2Client
@RestController
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // @Autowired
    // OAuth2ClientContext oauth2ClientContext;

//Different from spring boot example which returns a map with name as a key and principle.name as a value
    @RequestMapping({"/user", "/me"})
    public Principal user(Principal principal) {
        return principal;
    }

    @Override

    public void configure(AuthenticationManagerBuilder builder)
          throws Exception {
      builder.inMemoryAuthentication()
             .withUser("joe")
             .password("123")
             .roles("USER");
  

//     protected void configure(HttpSecurity http) throws Exception {
//         // @formatter:off
// http
//     .authorizeRequests()
//         .anyRequest().authenticated()
//         .and()
//     .formLogin()
//         .and()
//     .httpBasic().disable();

        // http.antMatcher("/**").authorizeRequests()
        //         .antMatchers("/", "/login**", "/webjars/**").permitAll()
        //         .anyRequest().authenticated().and().exceptionHandling()
        //         .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
        //         .logoutSuccessUrl("/logouturl_placeholder").permitAll().and().csrf()
        //         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        //         .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    // @Bean
    // public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    //     FilterRegistrationBean registration = new FilterRegistrationBean();
    //     registration.setFilter(filter);
    //     registration.setOrder(-100);
    //     return registration;
    // }



    // @Bean
    // @ConfigurationProperties("google")
    // public ClientResources google() {
    //     ClientResources clientResources = new ClientResources();
    //     return clientResources;
    // }

    // @Bean
    // @ConfigurationProperties("github")
    // public ClientResources github() {
    //     return new ClientResources();
    // }

    // @Bean
    // @ConfigurationProperties("facebook")
    // public ClientResources facebook() {
    //     return new ClientResources();
    // }
    // private Filter ssoFilter() {
    //     OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter(
    //             "/login/google");
    //     OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
    //     googleFilter.setRestTemplate(googleTemplate);
    //     googleFilter.setTokenServices(
    //             new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId()));
    //     return googleFilter;
    // }
    // private Filter ssoFilter(ClientResources client, String path) {
    //     OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
    //             path);
    //     OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
    //     filter.setRestTemplate(template);
    //     filter.setTokenServices(new UserInfoTokenServices(
    //             client.getResource().getUserInfoUri(), client.getClient().getClientId()));
    //     return filter;
    // }
    // private Filter ssoFilter() {
    //     CompositeFilter filter = new CompositeFilter();
    //     List<Filter> filters = new ArrayList<>();
    //     // filters.add(ssoFilter(facebook(), "/login/facebook"));
    //     // filters.add(ssoFilter(github(), "/login/github"));
    //     filters.add(ssoFilter(google(), "/login/google"));
    //     filter.setFilters(filters);
    //     return filter;
    // }
//From spring boot example  
    // @Bean
    // @ConfigurationProperties("google")
    // public ClientResources google() {
    //     ClientResources clientResources = new ClientResources();
    //     return clientResources;
    // }
//Original below, unsure why two properties    
    // @Bean
    // @ConfigurationProperties("google.client")
    // public AuthorizationCodeResourceDetails google() {
    //     return new AuthorizationCodeResourceDetails();
    // }

    // @Bean
    // @ConfigurationProperties("google.resource")
    // public ResourceServerProperties googleResource() {
    //     return new ResourceServerProperties();
    // }

// from spring boot example, not sure why it is missing
    // @Configuration
    // @EnableResourceServer
    // protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    //     @Override
    //     public void configure(HttpSecurity http) throws Exception {
    //         // @formatter:off
    //         http
    // .authorizeRequests()
    //     .anyRequest().authenticated()
    //     .and()
    // .formLogin()
    //     .and()
    // .httpBasic().disable();
    //         //http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
    //         // @formatter:on
    //     }
    // }
}
