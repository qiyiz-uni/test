/**   
* @Title: DictionaryServer.java
* @Package server
* @Description: This is a multi-thread dictionary server 
* @author Qiyi Zhang 1137926 qiyizhang@student.unimelb.edu.au
* @date April 11, 2021
*/

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.BindException;
import java.net.InetAddress;
import java.util.HashMap;

import javax.net.ServerSocketFactory;

public class DictionaryServer {
	// Dictionary data the stores in the local memory and shared by threads
	private static HashMap<String, String> dictionary = new HashMap<String, String>(); 
	// Identifies the user number connected
	private int counter = 0; 
	private int port = 0;
	private String ip = null;
	private String filepath = null;
	private static ServerGUI gui = null;
	
	
	public static void main(String[] args) {
		DictionaryServer dictionaryServer = new DictionaryServer();
		dictionaryServer.run(args);
	}
	
	public void run(String[] args) {
		try {
			port = Integer.parseInt(args[0]);
			if (port > 65535 | port < 1024 | port < 0) {
				System.out.println("ERROR - The port number must be chosen between 1024 to 65535");
				System.exit(-1);
			}
			filepath = args[1];
			initialiseDict(filepath);
			connect(port);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR - Lack of parameters!\nUsage: java -jar DictionaryServer.jar <port-number> <dictionary-file>, e.g. java -jar DictionaryServer.jar 4444 /Users/jenny/Downloads/COMP90015/Assignment/A1/initial.txt ");
		} catch (NumberFormatException e) {
			System.out.println("ERROR - The first argument must be an integer, e.g. 4444");
		}
	}
	
	public void runGUI() {
		try {
			gui = new ServerGUI(ip, port);
			gui.getFrame().setVisible(true);
		} catch (Exception e) {
			System.out.printf("ERROR - ", e.getMessage());
		}
	}
	
	
	public void initialiseDict(String filepath) {
		BufferedReader br = null;
		try {
            File file = new File(filepath);
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0].trim();
                String meaning = parts[1].trim();
                if (!name.equals("") && !meaning.equals(""))
                    dictionary.put(name, meaning);
            }
        } catch (FileNotFoundException e) {
        	System.out.printf("WARNING - No such file exists, the default dictionary will be used.\n");
        	initialiseDict("initial.txt");
        } catch (IOException e) {
        	System.out.printf("ERROR - ", e.getMessage());
		} finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) { };
            }
        }
	}
	
	/** 
	 * TCP connection
	 * Assign a new thread for each request 
	 * Connect to the dictionary data and reply to client
	 * Keep updating the client status and showing on the server GUI
	 * @param port: port number 
	 */
	public void connect(int port) {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(port))
		{
			ip = InetAddress.getLocalHost().getHostAddress();
			runGUI();
			gui.getTextArea().append("Server running...\n");
			gui.getTextArea().append("Waiting for client connection...\n");
			
			while(true)
			{
				Socket client = server.accept();
				counter++;
				gui.getTextArea().append("[" + counter + "]: Applying for request!\n");
							
				// Thread per request
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
		} catch (BindException e) {
			System.out.printf("ERROR - The port %s is using now, please try another one.\n", port);
		} catch (IOException e) {  
			System.out.printf("ERROR - ", e.getMessage());
		} 
	}
	
	/** 
	 * Thread operation
	 * Concurrently accessing the dictionary data
	 * @param client: client socket
	 */
	private static void serveClient(Socket client)
	{
		try(Socket clientSocket = client)
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		    String line = reader.readLine();
		    String[] result = parseExecution(line);
		    gui.getTextArea().append("Request: "+ result[1] + "\n");
		    writer.write(result[0]);
		    gui.getTextArea().append("Status: " + result[0] + "\n");
		    writer.flush();
		    reader.close();
		    writer.close();
		} 
		catch (IOException e) 
		{
			System.out.printf("ERROR - ", e.getMessage());
		}
	}
	
	/** 
	 * Helper methods for thread operation
	 * Synchronized access to database to ensure consistency
	 * @param client: client socket
	 */
	public static String[] parseExecution(String line) {
		String [] elements = line.split(":");
		String operator = null;
		String word = null;
		String meaning = null;
		String parsed = null;
		String result = null;
		
		if (elements.length == 2) {
			operator = elements[0];
			word = elements[1];
			parsed = operator + " " + word;
		} else if (elements.length == 3) {
			operator = elements[0];
			word = elements[1];
			meaning = elements[2];
			parsed = operator + " " + word + " " + meaning;
		} else {
			throw new IllegalArgumentException("ERROR - Parsing error");
		}
		switch (operator) {
			case "search" :
				result = search(word);
				break;
			case "add":
				result = add(word, meaning);
				break;
			case "remove":
				result = remove(word);
				break;
			case "update":
				result = update(word, meaning);
				break;
			default:
				throw new IllegalArgumentException("ERROR - Invalid functional operation");
		}
		String[] resultArr = {result, parsed};
		return resultArr;
	}
	
	
	public static String search(String word) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			return dictionary.get(word);
		} else {
			String str = "FAIL";
			return str;
		}
	}

	
	public static synchronized String add(String word, String meaning) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			String str = "FAIL";
			return str;
		} else {
			dictionary.put(word, meaning);
			String str = "SUCCESS";
			return str;
		}
	}

	
	public static synchronized String remove(String word) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			dictionary.remove(word);
			String str = "SUCCESS";
			return str;
		} else {
			String str = "FAIL";
			return str;
		}
	}

	
	public static synchronized String update(String word, String meaning) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			dictionary.replace(word, dictionary.get(word), meaning);
			String str = "SUCCESS";
			return str;
		} else {
			String str = "FAIL";
			return str;
		}
	}

}
