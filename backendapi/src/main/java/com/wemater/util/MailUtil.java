package com.wemater.util;

import org.apache.commons.codec.binary.Base64;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

import com.wemater.exception.AuthException;

public class MailUtil {


	
	public String  getMailHTML(String toAddress,String username){
		 String catString=username+":"+toAddress;
		  String url="http://localhost:8080/wematerClient-1/home/user/signup?verify="+new String(Base64.encodeBase64(catString.getBytes()));
		
		return 	"<html><head>	<meta name='viewport' content='width=device-width, initial-scale=1, user-scalable=no'>"+
				"</head>	<body>"+
			 "<table style='width: 100%; background-color: #f1f1f1;color: #333; '>"
			 + "<tr style='background-color: #222;'>"
			 + "<td style='border:1px solid #111; color: #f1f1f1; font-size: 1rem; text-align:center; padding:1em;"
			 + "text-transform: uppercase; '><h1><a style='text-decoration:none; color:#f1f1f1'; href='wemater.org'>wemater.org</a></h1></td>"
			 + "</tr>"
			 + "<td > <h2 style='text-transform: capitalize; color: #333; padding:.3em; '>"
			 + "Hello "+username
			 + " Welcome aboard</h2>"
			 + "<p style='font-size:1rem; margin-bottom:1em;'>For security reasons we have to confirm your email address.</p></td></tr>"
			 + "<tr style='width:100%; text-align:center;'>"
			 + "<td><a style='border:2px solid #222; text-decoration: none; color: #222; padding:.8em; border-radius: 10px;'"
			 + "href='"
			 +url
			 +"'>Verify your email</a></td></tr>"
			 + "<tr style='width:100%; text-align:center;'>"
			 + "<td> <p style='margin-top: 1em;'>Incase you are unable to verify. Copy paste the below link and open in a browser</p>"
			 + "<p style=' display: block; text-align: left;  min-height: 100px; background-color: #222; padding:1em;"
			 + "font-size:1rem; color:rgba(0,100,124,.8);'>"
			 + "<a style='text-decoration:none; color:color:rgba(0,80,124,.9);'>"+url+"</a>"
			 + " </p> </td></tr>"
			 + "<tr><td style='width:100%; text-align:center;'>"
			 + "<p style='text-transform:capitalize;text-decoration:none;'><a style='text-decoration:none; color:#333';>find more on wemater.org</a></p></td></tr>"
			 + "<tr style='width:100%; text-align:center;'><td>"
			 + "<a style='border: 1px solid #222; display:inline-block; width:90%; background-color: #222;"
			 + "text-transform:capitalize; padding:1em; color:rgba(0,80,124,.9); text-decoration:none;' href='EXPLORE-link here'>"
			 + "explore articles</a></td>"
			 + "</tr> <tr style='width:100%; text-align:center;'>	<td>"
			 + "<a style='border: 1px solid #222; display:inline-block; width:90%; background-color: #222;"
			 + "text-transform:capitalize; padding:1em; color:rgba(0,80,124,.9); text-decoration:none;'"
			 + "href='LAYESY HRE'>see what is latest</a></td> </tr></table>	</body> </html>";
	}

	
	public  void sendMail(String toAddress,String username){
		
		String message = getMailHTML(toAddress, username);
	    Email email = new Email.Builder()
	                    .from("wemater.org", "irshad.mike@gmail.com")
						.to("irshad", toAddress)
						.subject("complete registeration")
						.textHTML(message)	
						.build();

	Mailer mailer =new Mailer("smtp.gmail.com", 587, "irshad.mike@gmail.com", "International0401", TransportStrategy.SMTP_TLS);
	mailer.sendMail(email);

	System.out.println("Email sent");
	 }	
	
	public String[] getVerificationParams(String encodedAuth){
		if(encodedAuth == null) throw new AuthException("verify URL is NULL or EMPTY");
		String decodedAuth =new String(Base64.decodeBase64(encodedAuth.getBytes()));
		String[] params = decodedAuth.split(":");
		if(params.length != 2) throw new AuthException("Verify URL is not valid");
		return params;

	}
	
	

}
