/**   
* @Title: ClientGUI.java
* @Package client
* @Description: This is a GUI for client to search, remove, add, or update words on the dictionary
* @author Qiyi Zhang 1137926 qiyizhang@student.unimelb.edu.au
* @date April 11, 2021
*/


package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		GridBagLayout gridBagLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gridBagLayout);
		frame.setTitle("Client View");
		
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
		
		JLabel wordLabel = new JLabel("Word:");
		
		GridBagConstraints gbc_wordLabel = new GridBagConstraints();
		gbc_wordLabel.insets = new Insets(5, 5, 0, 0);
		gbc_wordLabel.weightx = 0.5;
		gbc_wordLabel.gridwidth = 4;
		gbc_wordLabel.gridheight = 1;
		gbc_wordLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_wordLabel.gridx = 0;
		gbc_wordLabel.gridy = 0;
		frame.getContentPane().add(wordLabel, gbc_wordLabel);
		
		wordField = new JTextField();
		wordField.setColumns(10);
		
		GridBagConstraints gbc_wordField = new GridBagConstraints();
		gbc_wordField.insets = new Insets(0, 0, 0, 0);
		gbc_wordField.weightx = 0.5;
		gbc_wordField.gridwidth = 4;
		gbc_wordField.gridheight = 1;
		gbc_wordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_wordField.gridx = 0;
		gbc_wordField.gridy = 1;
		frame.getContentPane().add(wordField, gbc_wordField);
		
		JLabel definLabel = new JLabel("Definition of the word: ");
		
		GridBagConstraints gbc_definLabel = new GridBagConstraints();
		gbc_definLabel.insets = new Insets(5, 5, 0, 0);
		gbc_definLabel.weightx = 0.5;
		gbc_definLabel.gridwidth = 4;
		gbc_definLabel.gridheight = 1;
		gbc_definLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_definLabel.gridx = 0;
		gbc_definLabel.gridy = 2;
		frame.getContentPane().add(definLabel, gbc_definLabel);
		
		meaningPane = new JTextArea();
		
		GridBagConstraints gbc_meaningPane = new GridBagConstraints();
		gbc_meaningPane.insets = new Insets(5, 5, 0, 5);
		gbc_meaningPane.weightx = 1;
		gbc_meaningPane.weighty = 1;
		gbc_meaningPane.gridwidth = 4;
		gbc_meaningPane.gridheight = 1;
		gbc_meaningPane.fill = GridBagConstraints.BOTH;
		gbc_meaningPane.gridx = 0;
		gbc_meaningPane.gridy = 3;
		frame.getContentPane().add(meaningPane, gbc_meaningPane);
		
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(6, 6, 6, 6);
		gbc_btnAdd.fill = GridBagConstraints.BOTH;
		gbc_btnAdd.ipady = 20;
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 4;
		gbc_btnAdd.weightx = 1.0;
		frame.getContentPane().add(btnAdd, gbc_btnAdd);
		
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(6, 6, 6, 6);
		gbc_btnSearch.fill = GridBagConstraints.BOTH;
		gbc_btnSearch.ipady = 20;
		gbc_btnSearch.gridx = 1;
		gbc_btnSearch.gridy = 4;
		gbc_btnSearch.weightx = 1.0;
		frame.getContentPane().add(btnSearch, gbc_btnSearch);
		
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(6, 6, 6, 6);
		gbc_btnRemove.fill = GridBagConstraints.BOTH;
		gbc_btnRemove.ipady = 20;
		gbc_btnRemove.gridx = 2;
		gbc_btnRemove.gridy = 4;
		gbc_btnRemove.weightx = 1.0;
		frame.getContentPane().add(btnRemove, gbc_btnRemove);
		
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(6, 6, 6, 6);
		gbc_btnUpdate.fill = GridBagConstraints.BOTH;
		gbc_btnUpdate.ipady = 20;
		gbc_btnUpdate.gridx = 3;
		gbc_btnUpdate.gridy = 4;
		gbc_btnUpdate.weightx = 1.0;
		frame.getContentPane().add(btnUpdate, gbc_btnUpdate);
		
	}
	

}
