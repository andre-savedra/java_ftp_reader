package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class SimpleFTP {

	private String host, username, password;
	private int port;
	private FTPClient ftp;
	private FTPFile[] files;
	private FileOutputStream fileOs;
	private InputStream fileIs;

	/**
	 * Create an instance of SimpleFTP.
	 */
	public SimpleFTP() {
		ftp = new FTPClient();
	}

	/** CONNECT **/
	public boolean connect(String ftp_host, int ftp_port, String ftp_username, String ftp_password) {

		host = ftp_host;
		username = ftp_username;
		password = ftp_password;
		port = ftp_port;

		boolean status = false;

		try {
			int reply;
			System.out.println("conectando ao host");
			
			ftp.connect(host, port);
			System.out.println("conectando ao username");
			ftp.login(username, password);

			System.out.println("Connected to " + host + ".");
			
			// After connection attempt, you should check the reply code to verify
			// success.
			reply = ftp.getReplyCode();
			//System.out.println(ftp.getReplyString());
			
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				// System.exit(1);
			}

			status = true;

		} catch (IOException e) {
			status = false;
			e.printStackTrace();
		}

		return status;
	}

	/** DISCONNECT **/
	public void disconnect() {

		if (ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				// do nothing
			}
		}
	}

	public boolean readFiles(String ftp_path) {

		boolean status = false;

		if (ftp.isConnected()) {

			try {
				files = ftp.listFiles(ftp_path);
				System.out.println(ftp.getReplyString());				

				fileOs = new FileOutputStream("Z:\\O7000.txt");

				ftp_path = "htdocs/share/GCODE.txt";
				status = ftp.retrieveFile(ftp_path, fileOs);
							
				System.out.println(ftp.getReplyString());				
				//status = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = false;
			}
		} else {
			status = false;
		}

		return status;
	}

	public FTPFile[] getFiles() {
		return files;
	}

	public InputStream getFileIs() {
		return fileIs;
	}

}
