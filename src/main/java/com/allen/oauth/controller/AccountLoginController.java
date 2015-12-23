package com.allen.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.allen.oauth.utils.OAuthHelper;

@Controller
@RequestMapping(value="user")
public class AccountLoginController {
	
	@RequestMapping(value="/login",method={RequestMethod.GET,RequestMethod.POST})
	public String Login(HttpServletRequest req,HttpServletResponse response){
		req.setAttribute("baiduAuthUrl", OAuthHelper.getInfo("baidu").getAuthUrl());
		//req.setAttribute("weichatAuthUrl", OAuthHelper.getInfo("wechat").getAuthUrl());
		req.setAttribute("githubAuthUrl", OAuthHelper.getInfo("github").getAuthUrl());
		req.setAttribute("wechatAuthUrl", OAuthHelper.getInfo("wechat").getAuthUrl());
		return "login";
	}
}
