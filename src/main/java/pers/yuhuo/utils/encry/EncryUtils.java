package pers.yuhuo.utils.encry;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import net.sf.json.JSONObject;

/**
 * 加密
 * @author Yu_Huo
 * 
 * https://1024tools.com/
 * 
 * 常用的加密算法
 * http://www.iteye.com/topic/1122076
 * 
 * HMAC加密
 * http://10960988.blog.51cto.com/10950988/1794928
 */
public class EncryUtils {
	
	/**
	 * MD5
	 */
	public static String md5(String str) {
		return encry(str, "MD5");
	}
	
    /**
     * sha256算法
     */
	public static String sha256(String str){
		return encry(str, "SHA");  //sha 默认使用sha256
	}
	
	
	
	
	
	private static String encry(String str , String encryName) {
		MessageDigest messageDigest = null;
		try {
			// 创建具有指定算法名称的信息摘要
			messageDigest = MessageDigest.getInstance(encryName);
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
		byte[] byteArray = messageDigest.digest();
		
		// 将得到的字节数组变成字符串返回
        return toHexString(byteArray);
	}

	
	
	public static String encodeHmacSHA256(String data, String key){
		String encry = null;
		try {
			encry = toHexString(encodeHmacSHA256(data.getBytes("UTF-8"), key.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
			encry = null;
		}
		return encry;
	}

	
    /**
     * 生成HmacSHA256加密数据
     * @param data  待加密数据
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static byte[] encodeHmacSHA256(byte[] data, byte[] key)
            throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        // 初始化mac
        mac.init(secretKey);
        // 执行消息摘要
        byte[] digest = mac.doFinal(data);
//        return new HexBinaryAdapter().marshal(digest);// 转为十六进制的字符串 大写
//        return toHexString(digest);
        return digest;
    }
    
    
	
	/**
	 * HmacSHA1
	 * @param key
	 * @param data
	 * @return
	 */
	public static byte[] encodeHmacSHA1(byte[] data , byte[] key) 
	{
	      try {
	          // 还原密钥
	          SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
	          // 实例化Mac
	          Mac mac = Mac.getInstance(signingKey.getAlgorithm());
	          // 初始化mac
	          mac.init(signingKey);
	          // 执行消息摘要
	          byte[] digest = mac.doFinal(data);
//	          return new HexBinaryAdapter().marshal(digest);// 转为十六进制的字符串 大写
//	          return toHexString(digest);
	          return digest;
	      } catch (NoSuchAlgorithmException e) {
	           e.printStackTrace();
	      } catch (InvalidKeyException e) {
	           e.printStackTrace();
	      }
	     return null;
	 }
    
    
    
    private static String  toHexString(byte[] bytes) {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
				strBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			else
				strBuff.append(Integer.toHexString(0xFF & bytes[i]));
		}
		return strBuff.toString();
    }
	
}
