import java.io.*;
import java.net.*;
import java.util.*;

public class Email{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		Socket smtpSock = null; //socket for sending
		Socket pop3Sock = null;	//socket for receiving
		DataOutputStream os = null; //outputting data to socket
		DataInputStream is = null; //inputting data to socket
		
		


		String input;
		String cont;
		System.out.println("WELCOME TO CHRIS'S EMAIL PROGRAM");
		System.out.println("*********************************");
		do{
		System.out.println("What would you like to do, type Send or Receive");
		input = in.nextLine();
		
		if(input.equals("Send") || input.equals("send"))
		{
			String responseline;

			try
			{
				smtpSock =new Socket("trig.sci.csueastbay.edu", 25); //opening smtp socket on port 25
				os = new DataOutputStream(smtpSock.getOutputStream()); //opening output stream on that socket
				is = new DataInputStream(smtpSock.getInputStream()); //opening input stream on that socket
			}catch(Exception e)
			{
				System.out.println("Host trig.sci.csueastbay is unknown");
			}

				if(smtpSock != null && os != null && is != null)
				{
					try
					{
						os.writeBytes("HELO algebra.sci.csueastbay.edu\r\n"); //HELO command
						System.out.println("HELO algebra.sci.csueastbay.edu\r\n");
						responseline = is.readLine();
						System.out.print(responseline + "\r\n");
						System.out.println("Enter your email address:");
						String e = in.nextLine();
						System.out.println("Mail From:<" + e + ">");
						os.writeBytes("Mail From:<" + e + ">\r\n "); //writing sender to socket
						responseline = is.readLine(); //getting response from socket
						System.out.print(responseline + "\r\n");
						System.out.println("Enter the receiver: ");
						String r = in.nextLine();
						os.writeBytes("RCPT To:<" + r + ">\r\n"); //writing receiver to socket
						responseline = is.readLine(); //getting response from socket
						System.out.print(responseline + "\r\n");
						System.out.println("RCPT To:<" + r + ">");
						os.writeBytes("Data\r\n"); //writing DATA command to socket
						System.out.println("Data");
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //starting input Buffer for message text
						String str;
						System.out.println("Enter body of mail, end with .");
						do{
						str = br.readLine();
						os.writeBytes("\r\n" + str); //writing message until receiving "." on its own line
						}while(!str.equals("."));
						os.writeBytes("\r\n.\r\n"); //writing "." to socket to end message
						responseline = is.readLine(); //getting response from socket
						System.out.print(responseline + "\r\n");
						System.out.println("QUIT");
						os.writeBytes("QUIT\r\n"); //QUIT command to finish
						os.close();
						is.close();
						smtpSock.close();
					}catch(Exception e)
					{
						System.out.println("Error sending mail");
					}	
				}
			

		}
		
		 
	    if(input.equals("Receive") || input.equals("receive"))
		{
			String responseline;
			try
			{
				pop3Sock =new Socket("trig.sci.csueastbay.edu", 110); //opening POP3 socket on port 110
				os = new DataOutputStream(pop3Sock.getOutputStream()); //opening output data stream on that socket
				is = new DataInputStream(pop3Sock.getInputStream()); //opening input data stream on that socket
			}catch(Exception e)
			{
				System.out.println("Host trig.sci.csueastbay is unknown");
			}

				if(pop3Sock != null && os != null && is != null)
				{
					try
					{
						responseline = is.readLine(); //getting response, verifying socket is ready
						System.out.print(responseline + "\r\n");
						System.out.println("Enter Username!");
						String u = in.nextLine();
						System.out.println("USER " + u);
						System.out.println("Enter Password!");
						String pass = in.nextLine();
						System.out.println("PASS " + pass);

						os.writeBytes("USER " + u +"\r\n"); //passing username to socket
						responseline = is.readLine();
						System.out.print(responseline + "\r\n");
						os.writeBytes("PASS " + pass + "\r\n"); //passing password to socket
						responseline = is.readLine();
						System.out.print(responseline + "\r\n");
						

						
						os.writeBytes("LIST\r\n"); //LIST command to view all emails
						while((responseline = is.readLine())!= null)
						{	
							System.out.print(responseline + "\r\n"); //keeping reading from socket until line w/ only "."
							if(responseline.equals("."))			 //telling program no more emails
								break;
						}
						String aa;
						do{
							System.out.println("What email would you like to read?");
							String e = in.nextLine();
							os.writeBytes("RETR " + e + "\r\n"); //telling socket what email to read
							while((responseline = is.readLine())!= null)
							{	
							System.out.print(responseline + "\r\n");  //reading email until line w/ only ".", meaning no more message left
							if(responseline.equals("."))
								break;
							}
							System.out.println("Would you like to read another? (Type Y or N)");
							aa = in.nextLine();
						}while(aa.equals("y") || aa.equals("Y"));
						String tes;
						System.out.println("Would you like to delete?(Type Y or N)");
						tes = in.nextLine();
						String stes;
						if(tes.equals("y") || tes.equals("Y"))
						{
							do{
								System.out.println("What email would you like to delete");
								String stre = in.nextLine();
								os.writeBytes("DELE " + stre + "\r\n");   //Calling DELE command to delete email that was specified 
								responseline = is.readLine();
								System.out.print(responseline + "\r\n");
								System.out.println("Would you like to delete another?(Type Y or N)");
								stes = in.nextLine();

							}while(stes.equals("Y") || stes.equals("y"));
						}
						os.writeBytes("QUIT\r\n"); //QUIT command to exit
						responseline = is.readLine(); //Resonponse from socket on Quit command
						System.out.print(responseline + "\r\n");
				
						os.close();
						is.close();
						pop3Sock.close();
					}catch(Exception e)
					{
						System.out.println("Error getting mail");
					}	


				}
		

		}


		System.out.println("Would you like to Send/Receive again? (y or n)"); //loop to continue sending and receiving if wanted
		cont = in.nextLine();
		}while(cont.equals("y") || cont.equals("Y"));

	}
}
