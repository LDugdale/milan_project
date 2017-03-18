package messengerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import communications.User;

public class CreateChatDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -2745656138044157242L;

	MessengerFrame frame;
	
	List<User> friends;
	
	JList<User> friendList;

	public CreateChatDialog(MessengerFrame frame, List<User> friends) {
		super(frame, "Create New User", true);
		
		if (frame != null) {
			Dimension parentSize = frame.getSize();
			Point p = frame.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		
		this.frame = frame;
		this.friends = friends;
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel lblAddFriend = new JPanel();
		JLabel lblAdd = new JLabel("Add a friend");
		lblAdd.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblAdd.setBounds(2, 25, 61, 16);
		lblAdd.setForeground(Color.WHITE);
		lblAddFriend.add(lblAdd);
		lblAddFriend.setBackground(new Color(51, 102, 153));
		getContentPane().add(lblAddFriend);
		
		
		JPanel username = new JPanel();
		username.setLayout(new GridLayout(1,2));
		
		username.add(new JLabel("           Friends: "));
		JPanel listUser = new JPanel();
		friendList = new JList<User>(friends.toArray(new User[0]));
		//friendList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		friendList.setCellRenderer(new UserCellRenderer());
		listUser.add(friendList);
		username.setBackground(Color.white);
		listUser.setBackground(Color.white);
		getContentPane().add(username);
		getContentPane().add(listUser);
		
		
		
		JPanel buttonPane = new JPanel();
		//buttonPane.setLayout(new GridLayout(1,2));
		
		JButton cancelButton = new JButton("Back");
		buttonPane.add(cancelButton);
		cancelButton.setBackground(Color.GRAY);
		cancelButton.setPreferredSize(new Dimension(150, 35));
		cancelButton.setForeground(Color.white);
		cancelButton.setFont(new Font("Ariel", Font.BOLD, 13));
		
		//JPanel buttonPane = new JPanel();
		
		JButton button = new JButton("Add");
		
		button.setBackground(new Color(51, 102, 153));
		button.setPreferredSize(new Dimension(150, 35));
		button.setForeground(Color.white);
		button.setFont(new Font("Ariel", Font.BOLD, 13));
		button.addActionListener(this);
		buttonPane.setBackground(Color.white);
		
		buttonPane.add(cancelButton);
		buttonPane.add(button);
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
		System.out.println("Button Pressed");
		
		List<User> selectedFriends = friendList.getSelectedValuesList();
		for(int i=0; i<selectedFriends.size(); i++) {
			System.out.println(selectedFriends.get(i));
		}
		
		frame.createChat(selectedFriends);
		
		setVisible(false);
		dispose();
	}
	
	class UserCellRenderer extends JLabel implements ListCellRenderer<User> {
		
		private static final long serialVersionUID = 8640111043325984186L;

		public UserCellRenderer() {
	         setOpaque(true);
	     }

	     @Override
	     public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {

			setText(value.getUsername());
			
			Color background;
			Color foreground;

			// check if this cell represents the current DnD drop location
			JList.DropLocation dropLocation = list.getDropLocation();
			if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

				background = new Color(51, 102, 153);
				foreground = Color.WHITE;

			} else if (isSelected) { // check if this cell is selected
				background = new Color(51, 102, 153);
				foreground = Color.WHITE;

				
			} else { // unselected, and not the DnD drop location
				background = Color.WHITE;
				foreground = Color.BLACK;
			}
			;

			setBackground(background);
			setForeground(foreground);

			return this;
		}
		
	}
}