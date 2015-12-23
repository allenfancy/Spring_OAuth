package com.allen.oauth.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class ThirdLoginFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String plat = getPlatName(request);
		String code = request.getParameter("code");
		toSuccessPage(plat, code, request, response);
	}

	// 授权成功后的跳转，因为演示程序第三方授权页有几种不同打开方式，成功页也不同，所以在这里根据不同平台分别处理
	private void toSuccessPage(String plat, String code, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Integer> platMap = new HashMap<String, Integer>();
		String url = "http://localhost" + ":" + request.getLocalPort() + request.getContextPath();
		platMap.put("baidu", 1);
		platMap.put("github", 2);
		platMap.put("wechat", 3);
		switch (platMap.get(plat)) {
		case 1: // 百度登录使用的是本页面跳转方式，授权成功后直接跳入user页
			url = url + "/user_page?code=" + code + "&plat=" + plat;
			System.out.println(url);
			response.sendRedirect(url);
			break;
		case 2:
			url = url + "/user_page?code=" + code + "&plat=" + plat;
			response.sendRedirect(url);
			break;
		case 3:
			url = url + "/user_page?code=" + code + "&plat=" + plat;
			response.sendRedirect(url);
			break;
		}
	}

	// 解析uri获取当前平台名
	private String getPlatName(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.substring(uri.lastIndexOf("/") + 1);
	}
}
