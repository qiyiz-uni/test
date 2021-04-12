/**   
* @Title: ClientGUI.java
* @Package client
* @Description: This is a GUI for client to search, remove, add, or update words on the dictionary
* @author Qiyi Zhang 1137926 qiyizhang@student.unimelb.edu.au
* @date April 11, 2021
*/


package client;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;

public class ClientGUI {

	private JFrame frame;
	private JTextArea meaningPane;
	private JTextField wordField;
	private DictionaryClient client;
	
	/**
	 * Create the application.
	 */
	public ClientGUI(DictionaryClient client) {
		this.client = client;
		initialize();
	}
	
	/**
	 * Helper methods
	 */
	public JFrame getFrame() {
		return frame;
	}
	
	
	private Boolean isValid(String word, String meaning, String operator) {
		if (word.equals("")) {
			JOptionPane.showMessageDialog(frame, "Please Enter a word.", "Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if ((operator == "ADD" && meaning.equals("")) || (operator == "UPDATE" && meaning.equals(""))) {
			JOptionPane.showMessageDialog(frame, "Please Enter the word's meaning.", "Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(450, 340));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client View");
		
		wordField = new JTextField();
		wordField.setColumns(10);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				String meaning = meaningPane.getText();
				if (isValid(word, meaning, "ADD")) {
					int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Add a new word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						String sendData = client.parseSendData("ADD", word, meaning);
						String receiveData = client.getReceiveData(sendData);
						if (receiveData.equals("SUCCESS")) {
							JOptionPane.showMessageDialog(frame, "Add Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
						} else if (receiveData.equals("FAIL")) {
							JOptionPane.showMessageDialog(frame, "Word Already Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
						} else if (receiveData.equals("CONNECTION REFUSED")) {
							JOptionPane.showMessageDialog(frame, "Connection Refused\nPlease try again with correct port!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("UNKNOWN HOST")){ 
							JOptionPane.showMessageDialog(frame, "Unknown Host\nPlease try again with correct host!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("TIMEOUT")) {
							JOptionPane.showMessageDialog(frame, "Socket Connection Time Out\nPlease try again with correct ip!", "Error", JOptionPane.ERROR_MESSAGE);
						} else {}
					}
				}
			}
		});
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				if (isValid(word, "", "SEARCH")) {
					String sendData = client.parseSendData("SEARCH", word, "");
					String receiveData = client.getReceiveData(sendData);
					if (receiveData.equals("FAIL")) {
						JOptionPane.showMessageDialog(frame, "Search Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
					} else if (receiveData.equals("CONNECTION REFUSED")) {
						JOptionPane.showMessageDialog(frame, "Connection Refused\nPlease try again with correct port!", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (receiveData.equals("UNKNOWN HOST")){ 
						JOptionPane.showMessageDialog(frame, "Unknown Host\nPlease try again with correct host!", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (receiveData.equals("TIMEOUT")) {
						JOptionPane.showMessageDialog(frame, "Socket Connection Time Out\nPlease try again with correct ip!", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						meaningPane.setText(receiveData);
					}
				}
			}
		});
		
		JButton btnRemove = new JButton("REMOVE");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				if (isValid(word, "", "REMOVE")) {
					int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to remove?", "Confirm Window", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						String sendData = client.parseSendData("REMOVE", word, "");
						String receiveData = client.getReceiveData(sendData);
						if (receiveData.equals("SUCCESS")) {
							JOptionPane.showMessageDialog(frame, "Remove Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
						} else if (receiveData.equals("FAIL")) {
							JOptionPane.showMessageDialog(frame, "Remove Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
						} else if (receiveData.equals("CONNECTION REFUSED")) {
							JOptionPane.showMessageDialog(frame, "Connection Refused\nPlease try again with correct port!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("UNKNOWN HOST")){ 
							JOptionPane.showMessageDialog(frame, "Unknown Host\nPlease try again with correct host!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("TIMEOUT")) {
							JOptionPane.showMessageDialog(frame, "Socket Connection Time Out\nPlease try again with correct ip!", "Error", JOptionPane.ERROR_MESSAGE);
						} else {}
					}
				}
			}
		});
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				String meaning = meaningPane.getText();
				if (isValid(word, meaning, "UPDATE")) {
					int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Update a word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						String sendData = client.parseSendData("UPDATE", word, meaning);
						String receiveData = client.getReceiveData(sendData);
						if (receiveData.equals("SUCCESS")) {
							JOptionPane.showMessageDialog(frame, "Update Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
						} else if (receiveData.equals("FAIL")) {
							JOptionPane.showMessageDialog(frame, "Update Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
						} else if (receiveData.equals("CONNECTION REFUSED")) {
							JOptionPane.showMessageDialog(frame, "Connection Refused\nPlease try again with correct port!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("UNKNOWN HOST")){ 
							JOptionPane.showMessageDialog(frame, "Unknown Host\nPlease try again with correct host!", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (receiveData.equals("TIMEOUT")) {
							JOptionPane.showMessageDialog(frame, "Socket Connection Time Out\nPlease try again with correct ip!", "Error", JOptionPane.ERROR_MESSAGE);
						} else {}
					}
				}
			}
		});
		
		JLabel lblMeaning = new JLabel("The Meaning(s) of the word: ");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblWord = new JLabel("Word:");
		
		meaningPane = new JTextArea();
		scrollPane.setViewportView(meaningPane);
		meaningPane.setLineWrap(true);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(5)
						.addComponent(lblWord, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addComponent(wordField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(lblMeaning, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(btnSearch, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(btnUpdate, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
					.addGap(5))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(lblWord, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addComponent(wordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(lblMeaning, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addGap(8))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	

}
