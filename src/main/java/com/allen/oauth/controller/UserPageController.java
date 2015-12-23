package com.allen.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.allen.oauth.dao.UserDao;
import com.allen.oauth.model.User;
import com.allen.oauth.utils.OAuthHelper;

@Controller
@RequestMapping(value = "user_page")
public class UserPageController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value="",method = RequestMethod.GET)
	public String userPage(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "code") String code,
			@RequestParam(value = "plat") String plat) {
		try {
			System.out.println(code + "   \n" + plat);
			User user = OAuthHelper.getUserInfo(plat, code);
			if (user != null) {
				User persistenceUser = userDao.findUserByOpenId(user.getOpenId());
				if (persistenceUser != null) {
					persistenceUser.setName(user.getName());
					userDao.updateOpenName(persistenceUser);
				} else {
					persistenceUser = userDao.saveOpenUser(user);
				}
				req.setAttribute("loginUser", persistenceUser);
				return "userPage";
			} else {
				req.setAttribute("errMsg", "授权时发生系统错误或操作被取消，请稍候再试！");
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("errMsg", "授权时发生系统错误或操作被取消，请稍候再试！");
			return "error";
		}
	}

	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logOut(HttpServletRequest req,HttpServletResponse res){
		req.setAttribute("loginUser", null);
		return "login";
	}
}
