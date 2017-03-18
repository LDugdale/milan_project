package messengerUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class EditUserDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -5791217417299917920L;
	
	MessengerFrame frame;
	
	//JTextArea passwordPane;
	//JTextArea usernameUpdatePane;
	//JTextArea passwordUpdatePane;
	JTextField passwordPane;
	JTextField usernameUpdatePane;
	JTextField passwordUpdatePane;
	JTextField confirmPassUpdatePane;
	
	JTextArea bioUpdatePane;

	public EditUserDialog(MessengerFrame frame) {
		super(frame, "Edit User", true);
		
		if (frame != null) {
			Dimension parentSize = frame.getSize();
			Point p = frame.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		
		this.frame = frame;
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(470,300));
		
		JPanel EditPane = new JPanel();
		EditPane.setLayout(new BoxLayout(EditPane, BoxLayout.Y_AXIS));
		EditPane.setBackground(Color.white);
		
		JPanel lblUpdate = new JPanel();
		JLabel lblUpdateTitle = new JLabel("Update my account");
		lblUpdateTitle.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblUpdateTitle.setBounds(2, 25, 61, 16);
		lblUpdate.add(lblUpdateTitle);
		lblUpdateTitle.setForeground(Color.white);
		lblUpdate.setBackground(new Color(51, 102, 153));
		//getContentPane().add(lblUpdate);
		EditPane.add(lblUpdate);
		
		
		JPanel password = new JPanel();
		//password.setLayout(new GridLayout(1,2));
		password.add(new JLabel("Current password: "));
		passwordPane = new JPasswordField();
		passwordPane.setText("");
		passwordPane.setColumns(21);
		passwordPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		password.setBackground(Color.white);
		password.add(passwordPane);
		//getContentPane().add(password);
		EditPane.add(password);
		
		JPanel usernameUpdate = new JPanel();
		//usernameUpdate.setLayout(new GridLayout(1,2));
		usernameUpdate.add(new JLabel("Update username: "));
		usernameUpdatePane = new JTextField();
		usernameUpdatePane.setText("");
		usernameUpdatePane.setColumns(21);
		usernameUpdatePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		usernameUpdate.add(usernameUpdatePane);
		usernameUpdate.setBackground(Color.white);
		//getContentPane().add(usernameUpdate);
		EditPane.add(usernameUpdate);
		
		JPanel passwordUpdate = new JPanel();
		//passwordUpdate.setLayout(new GridLayout(1,2));
		passwordUpdate.add(new JLabel("Update password: "));
		passwordUpdatePane = new JPasswordField();
		passwordUpdatePane.setText("");
		passwordUpdatePane.setColumns(21);
		passwordUpdatePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		passwordUpdate.add(passwordUpdatePane);
		passwordUpdate.setBackground(Color.white);
		//getContentPane().add(passwordUpdate);
		EditPane.add(passwordUpdate);
		
		JPanel ConfirmPassUpdate = new JPanel();
		//ConfirmPassUpdate.setLayout(new GridLayout(1,2));
		ConfirmPassUpdate.add(new JLabel("Confirm password: "));
		confirmPassUpdatePane = new JPasswordField();
		confirmPassUpdatePane.setText("");
		confirmPassUpdatePane.setColumns(21);
		confirmPassUpdatePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		ConfirmPassUpdate.add(confirmPassUpdatePane);
		ConfirmPassUpdate.setBackground(Color.white);
		//getContentPane().add(ConfirmPassUpdate);
		EditPane.add(ConfirmPassUpdate);
		
		JPanel bioUpdate = new JPanel();
		//bioUpdate.setLayout(new GridLayout(1,2));
		bioUpdate.add(new JLabel("               Update bio: "));
		bioUpdatePane = new JTextArea(1,21);
		bioUpdatePane.setLineWrap(true);
		bioUpdatePane.setWrapStyleWord(true);
		
		bioUpdatePane.setText("");
		bioUpdatePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		bioUpdate.add(bioUpdatePane);
		bioUpdate.setBackground(Color.white);
		//getContentPane().add(bioUpdate);
		EditPane.add(bioUpdate);
		
		
		JPanel buttonPane = new JPanel();
		//buttonPane.setLayout(new GridLayout(1,2));
		
		JButton cancelButton = new JButton("Back");
		buttonPane.add(cancelButton);
		cancelButton.setBackground(Color.GRAY);
		cancelButton.setPreferredSize(new Dimension(150, 35));
		cancelButton.setForeground(Color.white);
		cancelButton.setFont(new Font("Ariel", Font.BOLD, 13));
		
		//JPanel buttonPane = new JPanel();
		
		JButton button = new JButton("Update User");
		
		button.setBackground(new Color(51, 102, 153));
		button.setPreferredSize(new Dimension(150, 35));
		button.setForeground(Color.white);
		button.setFont(new Font("Ariel", Font.BOLD, 13));
		button.addActionListener(this);
		buttonPane.setBackground(Color.white);
        
		
		//buttonPane.setLayout(new GridLayout(1,2));
		/*JButton button = new JButton("Update User");
		buttonPane.add(button);
		button.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");*/
		buttonPane.add(cancelButton);
		buttonPane.add(button);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		//getContentPane().add(buttonPane);
		EditPane.add(buttonPane);
		
		JScrollPane scrollEditPane = new JScrollPane(EditPane);
		getContentPane().add(scrollEditPane);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pack();
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Update user:");
		
		String[] fields = new String[4];
		fields[0] = usernameUpdatePane.getText();
		if(passwordUpdatePane.getText().equals(confirmPassUpdatePane.getText())){
			fields[1] = passwordUpdatePane.getText();
		}
		else{
			JOptionPane.showMessageDialog(frame,
					"Make sure new password and corfim password match!",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}
		fields[2] = bioUpdatePane.getText();
		fields[3] = "";
		
		boolean[] edited = new boolean[4];
		edited[0] = fields[0].length() != 0;
		edited[1] = fields[1].length() != 0;
		edited[2] = fields[2].length() != 0;
		edited[3] = false;
		
		frame.editUser(passwordPane.getText(), edited, fields);
		
		setVisible(false);
		dispose();
	}
}