package pers.yuhuo.utils.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.junit.Test;

import com.jcraft.jsch.JSchException;

public class CommandTest {

	public void nativeCommandTest() throws IOException {
		Process pro = SSHShellUtils.nativeProcess(getTestComand());
		if (pro == null) {
			System.out.println("reload cmd process error!");
			return;
		}
		String code = "GBK";
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pro.getInputStream() , Charset.forName(code)));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(pro.getErrorStream() , Charset.forName(code)));
		String line = null;

		while ((line = stdError.readLine()) != null) {
			System.err.println(line);
		}

		while ((line = stdInput.readLine()) != null) {
			System.out.println(line);
		}
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public void sshCommandTest() {
		
		// 使用 jsch channelExec 远程登陆 调用shell 会存在  环境问题   路径问题 
		String command = "source /etc/profile; cd /home/test/work/cbt3/server/stable;  ./startgame.sh"; 
		String user = "test";
		String passwd= "test";
		String host = "192.168.1.208";
		try {
			SSHShellUtils.execCmd(command , user , passwd , host);
		} catch (JSchException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
	}

	public static String getTestComand() {
		Properties prop = System.getProperties();
		String curOs = prop.getProperty("os.name");
		if ("linux".equalsIgnoreCase(curOs)) {
			return "pwd";
		} else {
			return "cmd /c dir";
		}
	}
}
