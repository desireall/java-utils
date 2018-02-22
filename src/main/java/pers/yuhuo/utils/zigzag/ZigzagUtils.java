package pers.yuhuo.utils.zigzag;


/**
 * proto buff 中 和 varint 联合使用 减少 数值型 转化生成的 byte[] 的长度
 * 使用的  正数 映射 负数
 * 
 * http://blog.csdn.net/zgwangbo/article/details/51590186
 * @author Yu_Huo
 *
 */
public class ZigzagUtils {
	
	
	/**
	 * 生成的 int (符号位 放在最后一位)
	 * @param value
	 * @return
	 */
	public static int int2zigzagint(int value){
		return (value >> 31) ^ (value << 1);
	}
	
	public static int zigzag2Int(int value){
		return (value >>>1) ^ -(value & 1);
	}

	
	
	public static long long2zigzagint(long value){
		return (value >> 63) ^ (value << 1);
	}
	
	public static long zigzag2long(long value){
		return (value >>>1) ^ -(value & 1);
	}
	
	
	
	
	
	public static void main(String[] args) {
		int a = -147483647;
		int zigzaga = ZigzagUtils.int2zigzagint(a);
		System.err.println(String.format("%s 的 zigzag值 为 %s", a , zigzaga));
		a = ZigzagUtils.zigzag2Int(zigzaga);
		System.err.println(String.format("%s 的 反zigzag值 为 %s", zigzaga , a));
	
	
		long n = -2147483647l;
		long zigzagn = ZigzagUtils.long2zigzagint(n);
		System.err.println(String.format("%s 的 zigzag值 为 %s", n , zigzagn));
		n = ZigzagUtils.zigzag2long(zigzagn);
		System.err.println(String.format("%s 的 反zigzag值 为 %s", zigzagn , n));
	}
	
}
