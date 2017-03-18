package messengerUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font; 
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import communications.User;

public class UserInfoDialog extends JDialog {
	
	private static final long serialVersionUID = -5791217417299917920L;

	public UserInfoDialog(JFrame frame, User user) {
		super(frame, "Create New User", true);
		
		if (frame != null) {
			Dimension parentSize = frame.getSize();
			Point p = frame.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		System.out.println("User: " + user.getUsername() + " -> " + user.getBio());
		
		JPanel lblUpdate = new JPanel();
		JLabel lblUpdateTitle = new JLabel(user.getUsername());
		lblUpdateTitle.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblUpdateTitle.setBounds(2, 25, 61, 16);
		lblUpdate.add(lblUpdateTitle);
		lblUpdateTitle.setForeground(Color.white);
		lblUpdate.setBackground(new Color(51, 102, 153));
		//getContentPane().add(lblUpdate);
		getContentPane().add(lblUpdate);
		
		JPanel bioPane = new JPanel();
		//bioPane.setLayout(new GridLayout(1,1));
		bioPane.setBackground(Color.WHITE);
		//getContentPane().add(biolbl);
		//bioPane.add(biolbl);
		
		JTextArea bio = new JTextArea(user.getBio());
		bio.setEditable(false);
		bioPane.add(bio);
		
		getContentPane().add(bioPane);
		
		JPanel buttonPane = new JPanel();
		JButton button = new JButton("OK");
		button.setBackground(new Color(51, 102, 153));
		button.setPreferredSize(new Dimension(150, 35));
		button.setForeground(Color.white);
		button.setFont(new Font("Ariel", Font.BOLD, 13));
		buttonPane.add(button);
		buttonPane.setBackground(Color.white);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(button);
		
		getContentPane().add(buttonPane);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pack();
		
		setVisible(true);
	}

}