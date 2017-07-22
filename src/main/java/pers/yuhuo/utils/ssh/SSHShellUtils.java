package pers.yuhuo.utils.ssh;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;  
  
  
/**
 * JSch 是SSH2的一个纯Java实现。它允许你连接到一个sshd 服务器，使用端口转发，X11转发，文件传输等等
 * 
 * @author Yu_Huo
 *
 */
public class SSHShellUtils {  
    private static JSch jsch;  
    private static Session session;  
  
      
    /** 
     * 连接到指定的IP 
     *  
     * @throws JSchException 
     */  
    public static void connect(String user, String passwd, String host) throws JSchException {  
        jsch = new JSch();  
        session = jsch.getSession(user, host, 22);  
        session.setPassword(passwd);  
          
        java.util.Properties config = new java.util.Properties();  
        config.put("StrictHostKeyChecking", "no");  
        session.setConfig(config);  
          
        session.connect();  
    }  
  
    /** 
     * 执行相关的命令 
     * @throws JSchException  
     */  
    public static void execCmd(String command, String user, String passwd, String host) throws JSchException {  
        connect(user, passwd, host);
          
        BufferedReader reader = null;  
        Channel channel = null; 
  
        try {  
//        	String page_message = null;
                channel = session.openChannel("exec");  
                ((ChannelExec) channel).setCommand(command);  
                  
                channel.setInputStream(null);  
                ((ChannelExec) channel).setErrStream(System.err);  
  
                channel.connect();  
                InputStream in = channel.getInputStream();  
                
                reader = new BufferedReader(new InputStreamReader(in));  
                String buf = null;  
                while ((buf = reader.readLine()) != null) {  
                    System.out.println(buf);   // 处理 命令的返回信息
                }  
                
//                byte[] tmp=new byte[1024];
//                while(true){
//                  while(in.available()>0){
//                    int i=in.read(tmp, 0, 1024);
//                    if(i<0)break;
//                    page_message=new String(tmp, 0, i);
//                    System.out.print(page_message);
//                  }
//                  if(channel.isClosed()){
//                    if(in.available()>0) continue; 
//                    System.out.println("exit-status: "+channel.getExitStatus());
//                    break;
//                  }
//                  try{Thread.sleep(1000);}catch(Exception ee){}
//                }
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (JSchException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                reader.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            channel.disconnect();  
            session.disconnect();  
        }  
    }
    
    
    
	/**
	 * 本地执行
	 */
	public static Process nativeProcess(String cmds) {
		Process pro = null;
		try {
			pro = Runtime.getRuntime().exec(cmds);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return pro;
	}
    
    
    
	/**
	 * 执行本地命令
	 * @param cmds
	 * @return
	 */
	public static Process nativeProcessByProcessBuilder(String cmds , String filePath) {
		ProcessBuilder pro = new ProcessBuilder(cmds);
		pro.directory(new File(filePath));
		Process value = null;
		try {
			value = pro.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	
	
    
}  