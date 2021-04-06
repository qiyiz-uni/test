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
import java.util.HashMap;

import javax.net.ServerSocketFactory;

public class DictionaryServer {
	private static HashMap<String, String> dictionary = new HashMap<String, String>(); // Dictionary data the stores in the local memory and shared by threads
	private static int counter = 0; // Identifies the user number connected
	private static int port = 0;
	private static String file = null;
	private static Functions functions;
	
	
	public static void main(String[] args) {
		DictionaryServer dictionaryServer = new DictionaryServer();
		dictionaryServer.readArgs(args);
		dictionaryServer.initialiseDict(dictionary, file);
		dictionaryServer.setFunctions(new PlainFunctions(dictionary));
		dictionaryServer.connect(port);

	}
	
	
	public void readArgs(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java DictionaryServer <port-number> <dictionary-file>, e.g. java DictionaryServer 4444 initial.txt");
		} else {
			try {
				port = Integer.parseInt(args[0]);
				if (port > 65535 | port < 1024 | port < 0) {
					System.out.println("ERROR - The port number must be chosen between 1024 to 65535");
				}
			} catch (NumberFormatException e) {
				System.out.println("ERROR - The first argument must be an integer, e.g. 4444");
			} 
			file = args[1];
			if (!file.endsWith(".txt") && !file.endsWith(".json")) {
				System.out.println("ERROR - The second argument must be a string ends with .json or .txt, e.g. initial.txt");
			} 
		}
	}
	
	
	public void initialiseDict(HashMap<String, String> dictionary, String filename) {
		BufferedReader br = null;
		try {
            File file = new File(filename);
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0].trim();
                String meaning = parts[1].trim();
                if (!name.equals("") && !meaning.equals(""))
                    dictionary.put(name, meaning);
            }
//            System.out.println(dictionary);
        } catch (FileNotFoundException e) {
        	System.out.printf("ERROR - A %s occured: %s", e.getClass().toString(), e.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) { };
            }
        }
	}
	
	
	public void setFunctions(Functions newFunctions) {
		functions = newFunctions;
	}
	
	
	
	public void connect(int port) {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(port))
		{
			System.out.println("Waiting for client connection-");
			
			// Wait for connections
			while(true)
			{
				Socket client = server.accept();
				counter++;
				System.out.println("Client "+counter+": Applying for connection!");
							
				// Start a new thread for a connection // Thread per request
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static void serveClient(Socket client)
	{
		try(Socket clientSocket = client)
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		    String line = reader.readLine();
		    System.out.println("CLIENT: "+ line);
		    String result = parseExecution(line);
		    writer.write("Server: Hi Client " + counter + " !!!" + "The result is: " + result + "\n");
		    writer.flush();
		    reader.close();
		    writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Predefined message passing protocol is operator:word(:meaning)
	public static String parseExecution(String line) {
		String [] elements = line.split(":");
		String operator = null;
		String word = null;
		String meaning = null;
		String result = null;
		
		if (elements.length == 2) {
			operator = elements[0];
			word = elements[1];
		} else if (elements.length == 3) {
			operator = elements[0];
			word = elements[1];
			meaning = elements[2];
		} else {
			throw new IllegalArgumentException("ERROR - Parsing error");
		}
		System.out.println(operator);
		System.out.println(word);
		switch (operator) {
			case "search" :
				result = functions.query(word);
				break;
			case "add":
				result = functions.add(word, meaning);
				break;
			case "remove":
				result = functions.remove(word);
				break;
			case "update":
				result = functions.update(word, meaning);
				break;
			default:
				throw new IllegalArgumentException("ERROR - Invalid functional operation");
		}
		return result;
			
	}

}
