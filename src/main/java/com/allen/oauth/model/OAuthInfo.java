package com.allen.oauth.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.allen.oauth.model.User;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class OAuthInfo {

	private String authUrl;
	private String tokenUrl;
	private String userInfoApi;
	private String portraitUrl;

	private Map<String, String> params = new HashMap<String, String>();

	public OAuthInfo(String configFile) {
		InputStream is = null;
		SAXReader reader = new SAXReader();
		try {
			is = new FileInputStream(configFile);
			Document doc = reader.read(is);
			Element rootElement = doc.getRootElement();

			Element parentElement = rootElement.element("params");
			
			List<Element> elements = parentElement.elements();
			
			for (Element element : elements) {
				params.put(element.attributeValue("name"), element.attributeValue("value"));
			}
			authUrl = templateHandle(rootElement.element("authUrl").getText(), params);

			// 获取tokenUrl，替换掉固定模板值
			tokenUrl = templateHandle(rootElement.element("tokenUrl").getText(), params);

			// 获取userInfoApi
			userInfoApi = templateHandle(rootElement.element("userInfoApi").getText(), params);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 从返回的json对象中获取user信息
	 * 
	 * @param userNode
	 * @return
	 */
	public abstract User getUser(JsonNode userNode) throws IOException;

	/**
	 * 验证返回的json对象是否有效，因为有可能返回的是错误信息，此时无效
	 * 
	 * @param userNode
	 * @return
	 */
	public abstract boolean userDataValidate(JsonNode userNode);

	/**
	 * 引导用户浏览器跳转到授权页的路径
	 * 
	 * @return
	 */
	public String getAuthUrl() {
		return authUrl;
	}

	/**
	 * 获取完整的获取Token的URL地址
	 * 
	 * @param code
	 *            //授权后返回的code
	 * @return
	 */
	public String getTokenUrl(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		return templateHandle(this.tokenUrl, params);
	}

	/**
	 * 获取用户api访问完整路径
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getUserInfoApiUrl(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessToken", accessToken);
		return templateHandle(this.userInfoApi, params);
	}

	/**
	 * 用于模板处理
	 * 
	 * @param template
	 *            传递进来的参数
	 * @param param
	 *            解析的XML文件放入到Map<String,String>中
	 * @return
	 */
	private String templateHandle(String template, Map<String, String> params) {
		/**
		 * 示例: template =
		 * "http://test.com/auth?client_id=${cid}&redirect_uri=${ruri}&auth_type=code"
		 * ; regex = ${(.+?)}，匹配${cid}, ${ruri},匹配后group(0)为${cid}，group(1)为cid
		 */
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(template);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String key = matcher.group(1);
			String keyValue = params.get(key);
			if (keyValue == null) {
				continue;
			} else {
				matcher.appendReplacement(sb, keyValue);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
