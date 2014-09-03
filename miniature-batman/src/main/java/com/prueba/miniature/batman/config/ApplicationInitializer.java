package com.prueba.miniature.batman.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * @author Dennis Hernández djhv92@hotmail.com
 * @version 1.1
 * @since 2014-03-01
 */
public class ApplicationInitializer implements WebApplicationInitializer {


	/**
	 * Configure the given ServletContext with any servlets, 
	 * filters, listeners context-params and attributes necessary 
	 * for initializing this web application.
	 * @param servletContext {@link ServletContext}
	 * @throws
	 */
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		
		root.setServletContext(servletContext);
		root.scan("com.prueba.miniature.batman.config");
		root.refresh();
		
		servletContext.addListener(new ContextLoaderListener(root));
		servletContext.addListener(new RequestContextListener());
		
		
		final Dynamic servlet = servletContext.addServlet("spring", new DispatcherServlet(root));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		
		final Dynamic jerseyServlet = servletContext.addServlet("jerseyServlet", new SpringServlet());
		jerseyServlet.setLoadOnStartup(2);
		jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages", "com.prueba.miniature.batman");
		jerseyServlet.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		jerseyServlet.addMapping("/rest/*");
	
		servletContext.addFilter("passthroughFilter", new PassthroughFilter());
		servletContext.addFilter("wsFilter", new WSFilter());
	}
}