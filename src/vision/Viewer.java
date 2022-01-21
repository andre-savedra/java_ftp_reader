package vision;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.net.ftp.FTPFile;

import controller.SimpleFTP;
import controller.Threads;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;



public class Viewer extends JFrame {

	private JPanel contentPane;
	
	Threads refreshConnection;
	SimpleFTP FTP;
	boolean serverConnected;
	
	String ftp_host = "ftp.ezyro.com"; //"ftpupload.net";  //;
	String ftp_username = "ezyro_28393289";
	String ftp_password = "01040305";
	int ftp_port = 21;
	
	FTPFile ftp_files[];
	boolean r = false;
	private JTextArea txtFieldStatus;

	
	/**
	 * Create the frame.
	 */
	public Viewer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblTitle = new JLabel("Adaptador FMS INTERFACE");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblTitle, BorderLayout.NORTH);
		
		txtFieldStatus = new JTextArea();
		txtFieldStatus.setEditable(false);
		contentPane.add(txtFieldStatus, BorderLayout.CENTER);
		txtFieldStatus.setColumns(10);
		
		serverConnected = false;
		FTP = new SimpleFTP();		
		
		StringBuilder sb = new StringBuilder();

					
		refreshConnection = new Threads(new Runnable() {
			public void run() {

				while (true) {
					
					if (refreshConnection.isAllDone()) {
						return;
					}
					
					if(serverConnected) { //MANAGE FILES
						
						sb.delete(0, sb.length());							
						txtFieldStatus.setText(sb.toString());
						
						sb.append("LENDO ARQUIVOS... \n");
						txtFieldStatus.setText(sb.toString());

						r = FTP.readFiles("/htdocs/share");
						System.out.println("readFiles: " + r);					
						
						if(r) {
							sb.append("ARQUIVOS LIDOS!!! \n");
							txtFieldStatus.setText(sb.toString());
						}
						else {
							sb.append("FALHA DE LEITURA!!! \n");
							txtFieldStatus.setText(sb.toString());
						}
						
						ftp_files = FTP.getFiles();
						System.out.println("ARQUIVOS:");
						for(int i=0; i< ftp_files.length; i++)
						System.out.println(ftp_files[i].getName());
						
						

						sb.append("PRÓXIMA LEITURA EM 20 segundos... \n");
						txtFieldStatus.setText(sb.toString());
							 
						try {
							refreshConnection.sleep(20000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
					else { //MAKE FTP CONNECTION						
						
						try {
							sb.append("CONECTANDO AO SERVIDOR... \n");							
							txtFieldStatus.setText(sb.toString());
							
							r = FTP.connect(ftp_host, ftp_port, ftp_username, ftp_password);
							System.out.println("status connect: " + r);
							System.out.println("");

							if(r) {
								sb.append("SERVIDOR CONECTADO!!! \n");							
								txtFieldStatus.setText(sb.toString());
							}
							else {
								sb.append("FALHA CONEXÃO SERVIDOR!!! \n");							
								txtFieldStatus.setText(sb.toString());
							}
														
							sb.append("LENDO ARQUIVOS.... \n");							
							txtFieldStatus.setText(sb.toString());
							
							r = FTP.readFiles("/htdocs/share");
							System.out.println("readFiles: " + r);
							
							if(r) {
								sb.append("ARQUIVOS LIDOS!!! \n");							
								txtFieldStatus.setText(sb.toString());
							}
							else {
								sb.append("FALHA LEITURA!!! \n");							
								txtFieldStatus.setText(sb.toString());
							}
														
							ftp_files = FTP.getFiles();
							System.out.println("ARQUIVOS:");
							for(int i=0; i< ftp_files.length; i++)
							System.out.println(ftp_files[i].getName());
													
							
							sb.append("PRIMEIRA CONEXÃO FEITA COM SUCESSO! \n");							
							txtFieldStatus.setText(sb.toString());

							serverConnected = true;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
							System.out.println("ERRO CONEXÃO");
							
							sb.append("ERRO DE CONEXÃO!!! \n");							
							txtFieldStatus.setText(sb.toString());
							
							sb.append("PRÓXIMA TENTATIVA EM 5 segundos... \n");
							txtFieldStatus.setText(sb.toString());
						}
						
						/*try {
							Process p = Runtime.getRuntime().exec("COPY D:\\testeandre.txt D:\\AZURE\\testeandre.txt");
						} catch (IOException e1) {
							System.out.println("ERRO SCRIPT");
							e1.printStackTrace();
						}*/

						
						
						try {
							refreshConnection.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					
					

				} // while (true) thread
			} // public void run()
		});

		refreshConnection.start();
		
		
		
		
	}

}
