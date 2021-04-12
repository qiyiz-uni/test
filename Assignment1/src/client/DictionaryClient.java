/**   
* @Title: DictionaryClient.java
* @Package client
* @Description: This is a client that can connect to the multi-threaded dictionary server 
* @author Qiyi Zhang 1137926 qiyizhang@student.unimelb.edu.au
* @date April 11, 2021
*/

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class DictionaryClient {
	private String serverAddress = null;
	private int serverPort = 0;
	private ClientGUI gui = null;
	private Socket socket;
	
	
	public static void main(String[] args) {
		DictionaryClient dictionaryClient = new DictionaryClient();
		dictionaryClient.run(args);
	}
	
	public void run(String[] args) {
		try {
			serverAddress = args[0];
			boolean isValid = validate(serverAddress);
			if (!isValid) {
				System.out.println("ERROR - The first argument must be a valid ip address, e.g. 127.0.0.1");
				System.exit(-1);
			} 
			serverPort = Integer.parseInt(args[1]);
			if (serverPort > 65535 | serverPort < 1024 | serverPort < 0) {
				System.out.println("ERROR - The port number must be chosen between 1024 to 65535");
				System.exit(-1);
			}
			runGUI();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR - Lack of parameters!\nUsage: java -jar DictionaryClient.jar <server-address> <server-port>, e.g. java -jar DictionaryClient.jar 127.0.0.1 4444");
		} catch (NumberFormatException e) {
			System.out.println("ERROR - The second argument must be an integer, e.g. 4444");
		}
	}
	
	public boolean validate(String ip) {
	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	    return ip.matches(PATTERN);
	}
	
	private void runGUI() {
		try {
			this.gui = new ClientGUI(this);
			gui.getFrame().setVisible(true);
		} catch (Exception e) {
			System.out.printf("ERROR - ", e.getMessage());
		}
	}
	
	
	/** 
	 * Helper Method for GUI to interact with user 
	 * @param operator: button pressed on GUI
	 * @param word: word entered on GUI
	 * @param meaning: meaning entered on GUI
	 * @return: Predefined message passing protocol string -> operator:word:meaning
	 */
	public String parseSendData(String operator, String word, String meaning) {
		String result = null;
		switch(operator) {
		case "ADD" :
			result = "add:" + word.toLowerCase() + ":" + meaning;
			break;
		case "SEARCH" :
			result = "search:" + word.toLowerCase();
			break;
		case "REMOVE" :
			result = "remove:" + word.toLowerCase();
			break;
		case "UPDATE" :
			result = "update:" + word.toLowerCase() + ":" + meaning;
			break;
		default:
			break;
		}
		return result;
	}
	
	/** 
	 * Helper Method for GUI to interact with server
	 * @param sendData: parsed data message sent to server -> operator:word:meaning
	 * @return: data message received from server -> SUCCESS/FAIL/ERROR
	 */
	public String getReceiveData(String sendData) {
		String receiveData = connect(this.serverAddress, this.serverPort, sendData);
		return receiveData;
	}
	
	/** 
	 * TCP connection
	 * Send user operation to server and wait for response
	 * @param serverAddress: ip address 
	 * @param serverPort: port number 
	 * @param sendData: parsed data message sent to server -> operator:word:meaning
	 * @return: data message received from server -> SUCCESS/FAIL/ERROR
	 */
	private String connect(String serverAddress, int serverPort, String sendData) {
		String receiveData = null;
		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(serverAddress, serverPort), 1000); // Set connection request timeout time of 1 s
			socket.setSoTimeout(30000); // Set the read operation timeout time of 30 s
		    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		    writer.write(sendData + "\n");
	    	System.out.println("Data sent to Server--> " + sendData);
	    	writer.flush();
	    	receiveData = reader.readLine();
	    	System.out.println("Data received from Server--> " + receiveData);
	    	reader.close();
	    	writer.close();
		} catch (ConnectException e) {
			receiveData = "CONNECTION REFUSED";
		} catch (SocketTimeoutException e) {
			receiveData = "TIMEOUT";
		} catch (UnknownHostException e) {
			receiveData = "UNKNOWN HOST";
		} catch (IOException e) {
			System.out.printf("ERROR - ", e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.printf("ERROR - ", e.getMessage());
				}
			}
		}
		return receiveData;
	}

}
