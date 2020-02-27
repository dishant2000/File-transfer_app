import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.net.*;

public class FinaleServer extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinaleServer frame = new FinaleServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			while(true)
			 {System.out.println("Waiting for client");
	         ServerSocket ss = new ServerSocket(6125);
	         
	         Socket ClientSoc = ss.accept();
	         System.out.println("client found");
	         InputStreamReader isr = new InputStreamReader(ClientSoc.getInputStream());
	         BufferedReader br = new BufferedReader(isr);
	         String fname= br.readLine();
	         System.out.println(fname);
	        
	         byte[] myByte = new byte[6022386];
	         InputStream in = ClientSoc.getInputStream();
	         BufferedInputStream bis = new BufferedInputStream(in);
	         myByte = bis.readAllBytes();
	         FileOutputStream fos = new FileOutputStream("C:\\Users\\dishant\\Pictures\\Saved Pictures\\"+fname); 
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             bos.write(myByte);
             System.out.println("file recieved !!!");
             bos.flush();
             fos.flush();
             bos.close();
             fos.close();
             isr.close();
             br.close();
              ss.close();
              ClientSoc.close();
			 }  
             
             
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public FinaleServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
