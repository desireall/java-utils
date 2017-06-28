package pers.yuhuo.utils.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sun.misc.BASE64Encoder;

public class HttpTest {

	
	
	
	@Test
	public void oauth2getTokenBycode(){
		/**
		 * localhost:8080/spring-oauth-server/oauth/authorize?client_id=unity-client&redirect_uri=http%3a%2f%2flocalhost%3a8080%2fspring-oauth-server%2funity%2fdashboard&response_type=code&scope=read&state=123456
		 *
		 *
		 */
		String code = "b0c73520afb734770d0effc0285755bb";
		
//		String url = "http://localhost:8080/spring-oauth-server/oauth/token";
//		String params = "client_id=unity-client&client_secret=unity&grant_type=authorization_code&code="
//		                + code +"&redirect_uri=http%3a%2f%2flocalhost%3a8080%2fspring-oauth-server%2funity%2fdashboard";
		
		String url = "http://localhost:8080/authz/oauth/token";
		String params = "client_id=test&grant_type=authorization_code&code="
		                + code +"&redirect_uri=http://localhost:7777/spring-oauth-client/authorization_code_callback&state=09876999";
		
		Map<String, String> map = new HashMap<String, String>();
		
		/**
		 * 使用Http Basic Authorization     
		 * 
		 * contentType 的类型需要为 application/x-www-form-urlencoded
		 */
		@SuppressWarnings("restriction")
//		String encoding = new String(new BASE64Encoder().encode("unity-client:unity".getBytes()));
		String encoding = new String(new BASE64Encoder().encode("test:test".getBytes()));
		map.put("Authorization", "Basic " + encoding);
		System.err.println(HttpUtil.doPost(url, map, params, "application/x-www-form-urlencoded", false));
	}
	
	
	
	public void authzGetCode(){
		
		
	}
	
	
	
}
