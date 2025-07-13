package io.github.regulacao_marcarcao.regulacao_marcacao.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

        

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login")
                .setViewName("forward:/login.html");
        registry.addViewController("/index")
                .setViewName("forward:/index.html");
        registry.addViewController("/dashboard")
                .setViewName("forward:/index.html");
        registry.addViewController("/cadastrarsolicitacao")
        .setViewName("forward:/cadastrarsolicitacao.html");
        registry.addViewController("/listar")
        .setViewName("forward:/lista-solicitacoes.html");
        registry.addViewController("/logout")
        .setViewName("forward:/login.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
                
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        // Ao deixar prefixo e sufixo vazios, o resolver encaminha exatamente para a URL informada
        resolver.setPrefix("/");
        resolver.setSuffix("");
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        return resolver;
    }
}