package pers.yuhuo.utils.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 */
public class ZipUtils {
	public static final int COMPRESS_LEVEL = Deflater.BEST_COMPRESSION;
	private final static int s_send_msg_max_len = 32767;

	private final byte[] m_compressBuf = new byte[s_send_msg_max_len];

	/**
	 * 压缩
	 * 
	 * @param data
	 * @return
	 */
	public boolean compress(byte[] data) {
		Deflater compresser = new Deflater();
		compresser.reset();
		compresser.setLevel(COMPRESS_LEVEL);
		compresser.setInput(data);
		compresser.finish();
		boolean doOk = true;
		try {
			byte[] buf = new byte[1024];
			int m_effectLen = 0;
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				if (m_effectLen >= s_send_msg_max_len) {
					doOk = false;
					break;
				}
				System.arraycopy(buf, 0, m_compressBuf, m_effectLen, i);
				m_effectLen += i;
			}
		} catch (Exception e) {
			doOk = false;
			e.printStackTrace();
		}
		compresser.end();
		return doOk;
	}

	/**
	 * 解压缩
	 * 
	 * @param data
	 * @return 解压缩之后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];
		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);
		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length * 3);
		try {
			int decompressTimes = 0;
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);

				if (decompressTimes++ >= 1) {
					System.err.println("decompress death loop" + data);
					break;
				}
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		decompresser.end();
		return output;
	}

}
