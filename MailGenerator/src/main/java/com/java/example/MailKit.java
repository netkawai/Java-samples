package com.java.example;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MailKit implements Runnable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello");
		MailKit kit = new MailKit();
		SwingUtilities.invokeLater(kit);
				
		

		
	}
	
	
	static CharsetEncoder asciiEncoder = 
		      Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

	public static boolean isPureAscii(String v) {
		    return asciiEncoder.canEncode(v);
	}


	
	public static void createMessage(String to, String from, String subject, String body, List<File> attachments) {
	    try {
	        Message message = new MimeMessage((javax.mail.Session)null);
	        message.setFrom(new InternetAddress(from));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	        // message.setRecipients(RecipientType.CC, InternetAddress.parse(cc));
	        message.setSubject(subject);
	        // create the message part 
	        MimeBodyPart content = new MimeBodyPart();
	        String [] languages = new String[1];
	        String encoding;
	        // fill message
	        if(isPureAscii(body))
	        {
		        languages[0] = "en";
		        content.setContentLanguage(languages);
		        encoding = "us-ascii";
	        }else
	        {
		        languages[0] = "jp";
		        encoding = "utf-8";
	        }
	        content.setContentLanguage(languages);

	        content.setText(body,encoding,"plain");
	        content.setHeader("Content-Transfer-Encoding", "8bit");

	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(content);
	        // add attachments
	        for(File file : attachments) {
	            MimeBodyPart attachment = new MimeBodyPart();
	            DataSource source = new FileDataSource(file);
	            attachment.setDataHandler(new DataHandler(source));
	            attachment.setFileName(file.getName());
	            multipart.addBodyPart(attachment);
	        }
	        // integration
	        message.setContent(multipart);
	        // store file
	        message.writeTo(new FileOutputStream(new File("c:/users/netka/mail.eml")));
	    } catch (MessagingException ex) {
	        Logger.getLogger(MailKit.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        Logger.getLogger(MailKit.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MainFrame mainFrame = new MainFrame();
		mainFrame.setSize(300, 300);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	        	 mainFrame.dispose();
	          }        
	       });
		
		final JTextField userText = new JTextField();

		JButton okButton = new JButton("Gen");
	      okButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	     		File file = new File("C:/Users/netka/Desktop/UDHR_eng.pdf");
	    		List<File> files = new ArrayList<File>();
	    		files.add(file);
	    		createMessage("testa@example.com","testb@example.com","test subject",userText.getText(),files);
	         }
	      });
	    mainFrame.add(okButton);
	    mainFrame.add(userText);

		mainFrame.setVisible(true);
	}

}
