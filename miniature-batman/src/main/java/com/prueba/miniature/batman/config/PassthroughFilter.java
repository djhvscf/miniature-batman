package com.prueba.miniature.batman.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author Dennis Hernández djhv92@hotmail.com
 * @version 1.1
 * @since 2014-03-01
 */
@WebFilter(filterName="passthroughFilter",urlPatterns="/*")
public class PassthroughFilter implements Filter {

	final Logger logger = LoggerFactory.getLogger(PassthroughFilter.class);
	
	/**
	 * Elimina el objeto en memoria
	 */
	@Override
	public void destroy() {
	}

	/**
	 * Filtra las peticiones al servidor
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 * @throws
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest)request;
	    HttpServletResponse servletResponse = (HttpServletResponse) response;
	    chain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * Inicializa PaathroughFilter
	 * @param config FilterConfig
	 * @throws
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);
	}

}