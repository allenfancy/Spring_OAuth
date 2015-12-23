package com.allen.oauth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
/***
 * 
 * @author allen
 * 只能保证在一次请求只通过一次filter，而不需要重复执行
 */
public class CrossFilter extends OncePerRequestFilter{

	private static final Logger logger = Logger.getLogger(CrossFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.debug("进来过滤器了");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
			logger.debug("设置Access-Control-Allow-Methods");
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Origin, Content-Type, Accept, sat");
		}
		
		filterChain.doFilter(request, response);
	}

}
