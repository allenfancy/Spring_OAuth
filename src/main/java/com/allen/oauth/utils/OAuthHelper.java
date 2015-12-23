package com.allen.oauth.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.allen.oauth.model.OAuthInfo;
import com.allen.oauth.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuthHelper {
	// 私有化
	private OAuthHelper() {

	}

	private static final Map<String, OAuthInfo> infos = new HashMap<String, OAuthInfo>();

	static {
		String configBasePath = OAuthHelper.class.getClassLoader().getResource("/config").getPath();
		infos.put("baidu", new OAuthInfo(configBasePath + File.separator + "baidu.xml") {
			@Override
			public User getUser(JsonNode userNode) throws IOException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setOpenId("baidu_" + userNode.get("uid").asText());
				user.setName(userNode.get("uname").asText());
				return user;
			}

			@Override
			public boolean userDataValidate(JsonNode userNode) {
				// TODO Auto-generated method stub
				return userNode.get("uid") != null;
			}
		});

		infos.put("github", new OAuthInfo(configBasePath + File.separator + "github.xml") {
			@Override
			public User getUser(JsonNode userNode) throws IOException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setOpenId("github_" + userNode.get("id").asText());
				user.setName(userNode.get("name").asText());
				user.setAddress(userNode.get("location").asText());
				user.setPortraitUrl(userNode.get("avatar_url").asText());
				return user;
			}

			@Override
			public boolean userDataValidate(JsonNode userNode) {
				// TODO Auto-generated method stub
				return userNode.get("id") != null;
			}

		});
		/**
		 * {"openid":"o9pOCv9RmQVTCaP63_chXAB6u_oo","nickname":"allen","sex":1,"language":"zh_CN","city":"浦东新区","province":"上海","country":"中国",
		 * "headimgurl":"http://wx.qlogo.cn/mmopen/r6pNIajRw6D2yZTvrozWDVZI2rN8WsPqVWIu2RicAvLlwXiaYEelznnNWI3SaTAicGWWDxHKwMBUxvQ7dfO9ibzvJBeOeQS4OwGl/0","privilege":[]}
		 */
		infos.put("wechat", new OAuthInfo(configBasePath + File.separator + "wechat.xml") {
			@Override
			public User getUser(JsonNode userNode) throws IOException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setOpenId(userNode.get("openid").asText());
				user.setName(userNode.get("nickname").asText());
			/*	user.setAddress(userNode.get("location").asText());
				user.setPortraitUrl(userNode.get("avatar_url").asText());*/
				user.setPortraitUrl(userNode.get("headimgurl").asText());
				return user;
			}

			@Override
			public boolean userDataValidate(JsonNode userNode) {
				// TODO Auto-generated method stub
				return userNode.get("openid") != null;
			}

		});
	}

	/**
	 * 获取平台的信息
	 * 
	 * @param plat
	 * @return
	 */
	public static OAuthInfo getInfo(String plat) {
		return infos.get(plat);
	}

	/**
	 * 首先根据授权返回回来的code去换取access_token
	 * 其次如果access_token换取成功，则根据access_token去换取用户信息 最后把用户的信息存储在对应的数据库中
	 * 
	 * 获取用户信息
	 * 
	 * @param plat
	 * @param code
	 * @return
	 */
	public static User getUserInfo(String plat, String code) {
		OAuthInfo info = getInfo(plat);
		final ObjectMapper mapper = new ObjectMapper();
		try {
			String atokenUrl = info.getTokenUrl(code);
			String ret = httpGet(atokenUrl);
			JsonNode retNode = null;
			JsonNode atokenNode = null;
			
			String accessToken = null;
			if(ret.contains(":") && ret.contains(",")){
				retNode = mapper.readTree(ret);
				atokenNode = retNode.get("access_token");
				accessToken = atokenNode.asText();
			}else{
				String temp[] = ret.split("&");
				String temp2[] = temp[0].split("=");
				accessToken = temp2[1];
			}
			
			if (accessToken == null || "".equals(accessToken)) {
				logError(retNode, plat, code, "access_token");
				return null;
			} else {
				String apiUrl = info.getUserInfoApiUrl(accessToken);
				ret = httpGet(apiUrl);
				JsonNode userNode = mapper.readTree(ret);
				if (info.userDataValidate(userNode)) {
					return info.getUser(userNode);
				} else {
					logError(userNode, plat, atokenNode.asText(), "get_user");
					return null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 记录错误信息
	 * 
	 * @param retNode
	 * @param plat
	 * @param val
	 * @param step
	 */
	private static void logError(JsonNode retNode, String plat, String val, String phase) {
		System.err.println("error: " + plat + ", when " + phase + ", value is " + val);
		System.err.println(retNode.toString());
	}

	// 发起http get类型请求获取返回结果
	private static String httpGet(String urlstr) throws IOException {

		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);// post请求，需要向服务器写数据
		conn.setDoInput(true);// 因为要从服务器端获取响应数据，所以为true(默认值也是true)
		conn.connect();
		String ret = readResponse(conn,"UTF-8");
		return ret;
	}

	private static String readResponse(HttpURLConnection conn, String encoding) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;

		try {
			 InputStream in = conn.getInputStream();  
	         br = new BufferedReader(new InputStreamReader(in));
	         String line = "";
	         while((line = br.readLine())!=null){
	        	 sb.append(line);
	         }
	         in.close();
	         br.close();
	         conn.disconnect();
	        
	         return new String(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
