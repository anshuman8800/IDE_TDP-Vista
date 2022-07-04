package model;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
public class reply {
	public String cmdResult(String cmd) throws IOException, InterruptedException {
      Runtime run = Runtime.getRuntime();
      Process pr = run.exec(cmd);
      pr.waitFor();
      BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line = "";
      String result="";
      while ((line=buf.readLine())!=null) {
              result = result+"\n"+line;
      }
      return result+"\nRUN Successfully...";
      
  }
	public String commonReply(int n) throws IOException, URISyntaxException {
		String result ="";
		if(n==0) {
			result = "REPLY****************************\nHi! Your assistance is here\nWe see this is your first time chatting with us.\nWelcome to TDPVista.\n";
		}
		else if(n==1) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			   LocalDateTime now = LocalDateTime.now();  
			   result = dtf.format(now); 
		}
		else if(n==2) {
			result = "REPLY****************************\n"
					+ "What is the longest word in English language?\n"
					+ "'SMILE' Becasue there is a mile between first and last alphabet";
		}
		else if(n==3) {
			result ="REPLY****************************\n"
					+ "Father : Did you enjoy First day of school?\n"
					+ "Son : First day ?  Do you mean I have to go back tommorow?\n";
		}
		else if(n==4) {
			result = "REPLY****************************\n"
					+ "My name is BoT and What is your name?\n";
		}
		else if(n==5) {
			result = "REPLY****************************\n"
					+ "Traffic is Busy today...\n"
					+ "try another route!!!\n";
		}
		else if(n==6) {
			result = "REPLY****************************\n"
					+ "Your day is awesome Today\n"
					+ "Enjoy the day\n";
		}
		else if(n==7) {
			result = "REPLY****************************\nYes! I am very happy\n";
		}
		else if(n==8) {
			System.exit(0);
		}
		else if(n==9) {
			try {
	           Runtime.getRuntime().exec(new String[] {"cmd", "/K", "Start"});
	           result = "REPLY****************************\nOpening cmd...";
	        }
	        catch (Exception e)
	        {
	            result ="REPLY****************************\nSome Problem occur in Opening CMD";
	            e.printStackTrace();
	        }
		}
		else if(n==10) {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			    result ="REPLY****************************\nOpening Browser...";
				Desktop.getDesktop().browse(new URI("http://www.google.com"));
			}
			else result = "Some Problem occur in Opening Browser";
		}
		
		
		return result;
	}
	

}
