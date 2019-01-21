import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.taotao.common.utils.FtpUtil;

public class FTPtest {

	/*@Test
	public void test() throws Exception{
		//常见一个FtpClient对象
		FTPClient ftpClient = new FTPClient();
		//创建ftp链接
		ftpClient.connect("192.168.124.128",21);
		//登录ftp服务器,使用用户名和密码
		ftpClient.login("ftpuser", "ZUIEWANGGUAN");
		//读取本地文件
		FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\a\\Pictures\\哔哩哔哩图包\\1.png"));
		//设置上传的路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		//修改上传文件的格式
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		//上传文件(服务器端文档名,上传文档的inputStream)
		ftpClient.storeFile("head2.png", inputStream);
		//关闭连接
		ftpClient.logout();
	}*/
	@Test
	public void test() throws Exception{
		FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\a\\Pictures\\哔哩哔哩图包\\2.png"));
		FtpUtil.uploadFile("192.168.124.128", 21, "ftpuser", "ZUIEWANGGUAN", "/home/ftpuser/www/images", "/", "head3.jpg", inputStream);
	}
}
