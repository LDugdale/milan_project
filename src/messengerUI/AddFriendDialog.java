package messengerUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class AddFriendDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -3791316882515724018L;

	MessengerFrame frame;
	
	JTextField usernamePane;

	public AddFriendDialog(MessengerFrame frame) {
		super(frame, "Add Friend", true);
		
		if (frame != null) {
			Dimension parentSize = frame.getSize();
			Point p = frame.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		
		this.frame = frame;
		setResizable(false);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel lblFindPane = new JPanel();
		JLabel lblFind = new JLabel("Find a friend");
		lblFind.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblFind.setBounds(2, 25, 61, 16);
		lblFind.setForeground(Color.WHITE);
		lblFindPane.add(lblFind);
		lblFindPane.setBackground(new Color(51, 102, 153));
		getContentPane().add(lblFindPane);
		
		/*JPanel username = new JPanel();
		username.setLayout(new GridLayout(1,2));
		username.add(new JLabel("Username: "));
		usernamePane = new JTextArea();
		usernamePane.setText("");
		usernamePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		username.add(usernamePane);
		getContentPane().add(username);*/
		
		
		JPanel username = new JPanel();
		//confirmPassword.setLayout(new GridLayout(1,3));
		JLabel lblUsername = new JLabel("Add a Friend: ");
		username.add(lblUsername);
		usernamePane = new JTextField();
		usernamePane.setText("");
		usernamePane.setColumns(21);
		usernamePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		usernamePane.setBackground(Color.white);
		username.setBackground(Color.white);
		username.add(lblUsername);
		username.add(usernamePane);
		
		getContentPane().add(username);
		
		JPanel buttonPane = new JPanel();
		//buttonPane.setLayout(new GridLayout(1,2));
		
		JButton cancelButton = new JButton("Back");
		buttonPane.add(cancelButton);
		cancelButton.setBackground(Color.GRAY);
		cancelButton.setPreferredSize(new Dimension(150, 35));
		cancelButton.setForeground(Color.white);
		cancelButton.setFont(new Font("Ariel", Font.BOLD, 13));
		
		//JPanel buttonPane = new JPanel();
		
		JButton button = new JButton("Add Friend");
		
		button.setBackground(new Color(51, 102, 153));
		button.setPreferredSize(new Dimension(150, 35));
		button.setForeground(Color.white);
		button.setFont(new Font("Ariel", Font.BOLD, 13));
		button.addActionListener(this);
		buttonPane.setBackground(Color.white);
       
		buttonPane.add(cancelButton);
		buttonPane.add(button);
		
		
		
		/*JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(1,2));
		JButton button = new JButton("Add Friend");
		buttonPane.add(button);
		button.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);*/
		
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(buttonPane);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pack();
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Add friend from dialog: " + usernamePane.getText());
		
		frame.requestFriend(usernamePane.getText());
		
		setVisible(false);
		dispose();
	}
}