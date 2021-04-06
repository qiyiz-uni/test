package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class DictionaryClient {
	private static String serverAddress = null;
	private static int serverPort = 0;
	
	
	public static void main(String[] args) {
		DictionaryClient dictionaryClient = new DictionaryClient();
		dictionaryClient.readArgs(args);
		connect(serverAddress, serverPort);
	}
	
	
	public void readArgs(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java DictionaryClient <server-address> <server-port>, e.g. java DictionaryClient 127.0.0.1 4444");
		} else {
			serverAddress = args[0];
			boolean isValid = validate(serverAddress);
			if (!isValid) {
				System.out.println("The first argument must be a valid ip address, e.g. 127.0.0.1");
			} else {
				try {
					serverPort = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("The second argument must be an integer, e.g. 4444");
				} 
			}
		}
	}
	
	
	private static boolean validate(String ip) {
	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	    return ip.matches(PATTERN);
	}
	
	
	private static void connect(String serverAddress, int serverPort) {
		try(Socket socket = new Socket(serverAddress, serverPort);)
		{
		    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		    String sendData = "search:guru"; // message passing protocol e.g. add, search etc
		    String sendData = "add:love:an intense feeling of deep affection";
		    
		    writer.write(sendData + "\n");
	    	System.out.println("Data sent to Server--> " + sendData);
	    	writer.flush();
	    	System.out.println(reader.readLine());
	    	reader.close();
	    	writer.close();
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
