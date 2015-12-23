package com.allen.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;

import com.allen.oauth.utils.Sha1Util;

public class WeChatPay {

	public @ResponseBody Object pay(HttpServletRequest req,HttpServletResponse res){
		String code = req.getParameter("code");
		String state = req.getParameter("state");
		System.out.println("Code : " + code + "state : " + state);
		
		String noncestr = Sha1Util.getnonCeStr();
		String timestamp = Sha1Util.getTimeStamp();
		//state 可以传递给你的订单号，然后根据订单号 查询预付款金额。
		
		return null;
	}
}
