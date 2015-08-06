import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Buret {
	public BufferedReader in;
	public PrintWriter out;
	public Socket socket;
	public String name;
	public static final int PORTNUMBER = 4000; 
	public static final int MAX_STEPS = 93000;
	public String ipAddress = ""; 
	private int motorPosition;
	private int syringeSize;
	
	public void setSyringe(int val){
		syringeSize = val;
	}
	
	public Buret(String ip, String name){
		if (!ip.equals(ipAddress)){
	        try{
	    		socket = new Socket(ip, PORTNUMBER);
	            out = new PrintWriter(socket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            ipAddress = ip;
	            this.name = name;
	        	JOptionPane.showMessageDialog(null, "Connection Successful");
	            initializeBuret();
	        }
	        catch(UnknownHostException e){
	        	JOptionPane.showMessageDialog(null, "Don't know about host " + ipAddress);
	        }
	        catch(IOException e){
	        	JOptionPane.showMessageDialog(null, "Connection refused");
	        }
		}
	}
	
	public void goToTop(){
		deactivateValve();
		sendCommand("~S7001");
    	getReply(6);
		sendCommand("~S5200" + Integer.toString(MAX_STEPS));
    	String pos = getReply(11);
    	sendCommand("~S7000");
    	getReply(6);
    	motorPosition = Integer.parseInt(pos.substring(9, 15));
	}
	
	public void home(){
		activateValve();
		sendCommand("~S7001");
    	getReply(6);
		sendCommand("~S500");
    	getReply(6);
    	sendCommand("~S7000");
    	getReply(6);
    	motorPosition = 0;
    	try{
    		Thread.sleep(4000);
    	}
    	catch(InterruptedException e){
    	}
    	deactivateValve();
	}
	
	private void activateValve(){
		sendCommand("~S561");
    	getReply(5);
	}
	
	private void deactivateValve(){
		sendCommand("~S560");
    	getReply(5);
	}
	
	public void purgeBuret(){
		home();
		goToTop();
		home();
	}
	
	public void volume(double value){
		double valueStore = value;
		//Step 1: Go to Top
		double stepsToTop = ((1 - ((double) motorPosition)/MAX_STEPS)*syringeSize);
		if(!percent(valueStore/((double)syringeSize))){
			goToTop();
			valueStore -= stepsToTop;
			home();
		}
		else{
			return;
		}
		//Step 2: Purges
		if(valueStore > syringeSize){
			while (valueStore > syringeSize){
				valueStore -= syringeSize;
				purgeBuret();
			}
		}
		
		//Step 3: Volume left is less than a purge
		percent(valueStore/((double)syringeSize));
	}
	
	public boolean percent(double value){
		if (value > (1 - ((double) motorPosition)/MAX_STEPS)){
			return false;
		}
		activateValve();
		sendCommand("~S7001");
    	getReply(6);
		String newPos = Integer.toString((int) ((((double) motorPosition)/MAX_STEPS + value)*MAX_STEPS));
		String command = "~S520";
		for (int x = 0; x < (6-newPos.length()); x++){
			command += "0";
		}
		command += newPos;
		sendCommand(command);
		getReply(11);
		sendCommand("~S7000");
    	getReply(6);
		motorPosition = (int) ((((double) motorPosition)/MAX_STEPS + value)*MAX_STEPS); 
		return true;
	}
	
	public void initializeBuret(){
		sendCommand("~S770110000");
    	getReply(6);
		sendCommand("~S780005");
    	getReply(6);
		sendCommand("~S730050");
    	getReply(6);
		sendCommand("~S790020");
    	getReply(6);
		sendCommand("~S7100400");
    	getReply(6);
		sendCommand("~S72001000");
    	getReply(6);
		sendCommand("~S551");
    	getReply(5);
		sendCommand("~S561");
    	getReply(5);
		sendCommand("~S7001");
    	getReply(6);
		sendCommand("~S7101000");
    	getReply(6);
		sendCommand("~S72001000");
    	getReply(6);
		sendCommand("~S500");
    	getReply(6);
		sendCommand("~S7100400");
    	getReply(6);
		sendCommand("~S72001000");
    	getReply(6);
		sendCommand("~S520001000");
    	getReply(11);
		sendCommand("~S7000");
    	getReply(6);
		sendCommand("~S560");
    	getReply(5);
		MainFrame.field.setCaretPosition(MainFrame.field.getDocument().getLength());
	}
	
	private String getReply(int numChars){
		String reply = "Rx: ";
		int counter = 0;
		try{
			while (counter < numChars){
				reply += (char) in.read();
				counter++;
			}
		}
		catch(IOException e){
		}
		String eol = System.getProperty("line.separator");
		MainFrame.field.setText(MainFrame.field.getText() + reply + eol);
		MainFrame.field.update(MainFrame.field.getGraphics());
		return reply;
	}
	
	private void sendCommand(String cmd){
		String eol = System.getProperty("line.separator");
		MainFrame.field.setText(MainFrame.field.getText() + "Tx: " + cmd + eol);
		MainFrame.field.update(MainFrame.field.getGraphics());
		out.print(cmd);
		out.checkError();
	}
}
