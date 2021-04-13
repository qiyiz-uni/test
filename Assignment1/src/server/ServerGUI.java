/**   
* @Title: ServerGUI.java
* @Package server
* @Description: This is a GUI for server to monitor client's operation
* @author Qiyi Zhang 1137926 qiyizhang@student.unimelb.edu.au
* @date April 11, 2021
*/


package server;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerGUI {

	private JFrame frame;
	private JTextArea textArea;
	private JLabel ipLabel;
	private JLabel portLabel;

	/**
	 * Create the application.
	 */

	public ServerGUI(String ip, int port) {
		initialize(ip, port);
	}
	
	/**
	 * Helper methods
	 */
	public JFrame getFrame() {
		return frame;
	}
	
	
	public JTextArea getTextArea() {
		return textArea;
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String address, int port) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(450, 300));
		GridBagLayout gridBagLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gridBagLayout);
		frame.setTitle("Server View");
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.out.println("Server Close!");
			}
		});
		
		ipLabel = new JLabel("Address: " + address);
		
		GridBagConstraints gbc_ipLabel = new GridBagConstraints();
		gbc_ipLabel.insets = new Insets(10, 10, 0, 0);
		gbc_ipLabel.weightx = 0.5;
		gbc_ipLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipLabel.gridx = 0;
		gbc_ipLabel.gridy = 0;
		frame.getContentPane().add(ipLabel, gbc_ipLabel);
		
		portLabel = new JLabel("Port: " + port);
		
		GridBagConstraints gbc_portLabel = new GridBagConstraints();
		gbc_portLabel.insets = new Insets(0, 10, 0, 0);
		gbc_portLabel.weightx = 0.5;
		gbc_portLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_portLabel.gridx = 0;
		gbc_portLabel.gridy = 1;
		frame.getContentPane().add(portLabel, gbc_portLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.weightx = 1;
		gbc_scrollPane.weighty = 1;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
	}

}
