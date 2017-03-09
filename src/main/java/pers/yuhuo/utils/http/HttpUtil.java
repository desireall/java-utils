package pers.yuhuo.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 工具类。
 */
public class HttpUtil {

	// 系统错误
	public static final int SYS_ERROR = -1000;

	public static final int OK = 0;

	private static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void doAsynPost(final String url, final String body,
			boolean isHttps) {
		doAsynPost(url, null, body, isHttps);
	}

	public static void doAsynPost(final String url,
			final Map<String, String> header, final String body,
			final boolean isHttps) {
		pool.execute(new Runnable() {

			public void run() {
				HttpUtil.doPost(url, null, body, isHttps);
			}
		});
	}

	public static void doAsynPost(final String url, final String body,
			final HttpCallback callback, boolean isHttps) {
		doAsynPost(url, null, body, callback, isHttps);
	}

	public static void doAsynPost(final String url,
			final Map<String, String> header, final String body,
			final HttpCallback callback, final boolean isHttps) {
		pool.execute(new Runnable() {

			public void run() {
				String response = HttpUtil.doPost(url, header, body, isHttps);
				callback.callback(response);
			}
		});
	}

	public static void doAsynPost(Runnable runnable) {
		pool.execute(runnable);
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的内容
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的内容
	 */
	public static String doPost(String url, String body, boolean isHttps) {
		return doPost(url, null, body, isHttps);
	}

	public static String doPost(String url, Map<String, String> header,
			String body, boolean isHttps) {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			if (isHttps) {
				httpclient = wrapClient(httpclient);
			}
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			if (header != null) {
				for (String key : header.keySet()) {
					httppost.setHeader(key, header.get(key));
				}
			}
			if (body != null) {
				httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			}
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 读取配置信息。
	 * 
	 * @param key
	 *            Key值
	 * @return
	 * @throws Exception
	 */
	public static String getConfig(String key) throws Exception {
		try {
			InputStream in = HttpUtil.class
					.getResourceAsStream("/conf.Properties");
			Properties p = new Properties();
			p.load(in);
			return p.get(key).toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("配置文件不存在" + e.toString());
		}
		return "";
	}

	/**
	 * HTTPS
	 * 
	 * @param base
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static org.apache.http.client.HttpClient wrapClient(
			org.apache.http.client.HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] xcs,
						String string) {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String doGet(String url, Map<String, String> paramMap,
			boolean https) {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		// 拼接URL
		if (paramMap != null && paramMap.size() > 0) {
			sb.append("?");
			for (String key : paramMap.keySet()) {
				sb.append(key).append("=").append(paramMap.get(key))
						.append("&");
			}
			url = sb.substring(0, sb.length() - 1);
		}

		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {

			HttpClient httpclient = new DefaultHttpClient();
			if (https) {
				httpclient = wrapClient(httpclient);
			}
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			// 去掉最后一个&
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");

			response = httpclient.execute(httpGet);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					 e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}

	public static interface HttpCallback {

		public void callback(String response);
	}
}
