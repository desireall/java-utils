package pers.yuhuo.utils.encry;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密
 * 
 * @author Yu_Huo
 *
 */
public class EncryUtils {

	
	
	/**
	 * MD5
	 */
	public static String md5(String str) {
		return encry(str, "MD5");
	}
	
    /**
     * hmac(sha256)算法
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
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
	
	
	public static void main(String[] args) {
		
		
	}
	
}
