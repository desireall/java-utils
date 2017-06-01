package pers.yuhuo.utils.http;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 工具类 CloseableHttpClient 同步访问
 */
public class HttpUtil {

	private static final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000)
			.setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();

	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public static String doGet(String url, Map<String, String> paramMap, boolean https) {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		// 拼接URL
		if (paramMap != null && paramMap.size() > 0) {
			sb.append("?");
			for (String key : paramMap.keySet()) {
				sb.append(key).append("=").append(paramMap.get(key)).append("&");
			}
			url = sb.substring(0, sb.length() - 1);
		}
		HttpGet httpGet = new HttpGet(url);// 创建get请求
		return sendHttpGet(httpGet, https);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param params
	 *            参数
	 */
	public static String doPost(String httpUrl , Map<String, String> headerMap, String params, boolean https) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			if (headerMap != null) {
				for (String key : headerMap.keySet()) {
					httpPost.setHeader(key, headerMap.get(key));
				}
			}
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost, https);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param maps
	 *            参数
	 */
	public static String doPostWithParam(String httpUrl, Map<String, String> maps, boolean https) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost, https);
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet, boolean https) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			if (https) {
				PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
						.load(new URL(httpGet.getURI().toString()));
				DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
				httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			} else {
				httpClient = HttpClients.createDefault();
			}

			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				responseContent = null;
			}else{
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost, boolean https) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			if (https) {
				PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
						.load(new URL(httpPost.getURI().toString()));
				DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
				httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			} else {
				httpClient = HttpClients.createDefault();
			}
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				responseContent = null;
			}else{
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	
	
}
