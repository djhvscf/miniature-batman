package com.prueba.miniature.batman.config;

import java.util.List;
import java.util.Locale;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Dennis Hernández djhv92@hotmail.com
 * @version 1.1
 * @since 1.1
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.prueba.miniature.batman"})
public class ApplicationConfig extends WebMvcConfigurerAdapter {
    
	@Autowired
	SessionFactory sessionFactory;
	
	private final String suffix = ".jsp";
	private final String prefix = "/WEB-INF/views/";
	
	/**
	 * Obtiene los resources de la aplicación
	 * @param registry {@link ResourceHandlerRegistry}
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) 
	{
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	/**
	 * Add Spring MVC lifecycle interceptors for pre- and post-processing 
	 * of controller method invocations. Interceptors can be registered to 
	 * apply to all requests or be limited to a subset of URL patterns
	 * @param registry {@link InterceptorRegistry}
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) 
	{
		OpenSessionInViewInterceptor osivi = new OpenSessionInViewInterceptor();
		osivi.setSessionFactory(sessionFactory);
		registry.addWebRequestInterceptor(osivi);
	    registry.addInterceptor(getLocaleChangeInterceptor());
	}
	
	/**
	 * Configure the HttpMessageConverters to use in argument resolvers 
	 * and return value handlers that support reading and/or writing to the 
	 * body of the request and response. If no message converters are 
	 * added to the list, default converters are added instead.
	 * @param converters {@link HttpMessageConverter} 
	 */
	@Override
    public void configureMessageConverters( List<HttpMessageConverter<?>> converters ) 
	{
        converters.add(converter());
    }

	/**
	 * Implementation of HttpMessageConverter that can read and write JSON using Jackson's ObjectMapper. 
	 * @return {@link MappingJacksonHttpMessageConverter}
	 */
    @Bean
    MappingJacksonHttpMessageConverter converter() 
    {
    	return new MappingJacksonHttpMessageConverter();
    }
	
    /**
     * A strategy interface for multipart file upload resolution in 
     * accordance with RFC 1867. Implementations are typically usable 
     * both within an application context and standalone. 
     * @return {@link MultipartResolver}
     */
    @Bean
    public MultipartResolver multipartResolver() 
    {
        return new CommonsMultipartResolver();
    }
    
    /**
     * Permite el multi-lenguaje 
     * @return {@link ReloadableResourceBundleMessageSource}
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource()
    {
    	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:lenguaje/lenguaje");
        messageSource.setCacheSeconds(0);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
    /**
     * Carga el resource con el lenguaje correcto
     * @return {@link LocaleResolver}
     */
	@Bean
    public LocaleResolver localeResolver() 
	{
		 final CookieLocaleResolver cookieLocale = new CookieLocaleResolver();
		 cookieLocale.setDefaultLocale(new Locale("es"));
		 return cookieLocale;
    }
	
	/**
	 * Maneja el mapeo de controladores
	 * @return {@link RequestMappingHandlerMapping}
	 */
	@Bean 
	public RequestMappingHandlerMapping handlerMapping()
	{
		RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
		rmhm.setInterceptors(new Object[]{getLocaleChangeInterceptor()});
		return rmhm;
	}
	
	/**
	 * Obtiene el lenguje actual del usuario
	 * @return {@link LocaleChangeInterceptor}
	 */
	public LocaleChangeInterceptor getLocaleChangeInterceptor()
	{
		final LocaleChangeInterceptor locale = new LocaleChangeInterceptor();
		locale.setParamName("lang");
		return locale;
	}
	
	/**
	 * Resuelve las vista y su extensión
	 * @return {@link InternalResourceViewResolver}
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver()
	{
		InternalResourceViewResolver isvr = new InternalResourceViewResolver();
		isvr.setPrefix(prefix);
		isvr.setSuffix(suffix);
		return isvr;
	}
}