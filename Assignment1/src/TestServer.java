import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;
import java.util.*;



public class TestServer {
	
	static HashMap<String, String> dictionary = new HashMap<String, String>();
	
	public static void main(String[] args) {
		// read from command line
		int port = 0;
		String file = null;
		
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
			} else {
				initialiseDict(dictionary, file);
				connect(port);
			}
		}
		
	}
	
	public static void initialiseDict(HashMap<String, String> dictionary, String filename) {
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
        }
        catch (FileNotFoundException e) {
        	System.out.printf("ERROR - A %s occured: %s", e.getClass().toString(), e.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
		}
        finally {
  
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }
  
	}
	
	
	public static void connect(int port) {
		ServerSocket server = null;
		Socket client = null;
		
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			
			while (true) {
				client = server.accept(); 
				System.out.println("New client connected " + client.getInetAddress().getHostAddress());
				
				ClientHandler clientSock = new ClientHandler(client);
				new Thread(clientSock).start();
				
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class ClientHandler implements Runnable{
		private final Socket clientSocket;
		
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		@Override
		public void run() {
			BufferedReader in = null;
			BufferedWriter out = null;
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {
					System.out.printf("Sent from client: %s\n", line);
					out.write(line + "\n");
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
}
