import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClient {
	public static void main(String[] args) {
		// read from command line
		String serverAddress = null;
		int serverPort = 0;
		
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
				connect(serverAddress, serverPort);
			}
			
			
		}
	}
	
	public static boolean validate(String ip) {
	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	    return ip.matches(PATTERN);
	}
	
	public static void connect(String serverAddress, int serverPort) {
		Socket socket = null;
		try {
			socket = new Socket(serverAddress, serverPort);
			System.out.println("Connection established");

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			Scanner scanner = new Scanner(System.in);
			String line = null;

			while (!("exit").equalsIgnoreCase(line)) {
				line = scanner.nextLine();
				// Send the input string to the server by writing to the socket output stream
				out.write(line + "\n");
				out.flush();
				
				String received = in.readLine(); 
				System.out.println("Server replied: " + received);
			}
			scanner.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
