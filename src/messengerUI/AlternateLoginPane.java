package messengerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class AlternateLoginPane extends JPanel {
	
	private static final long serialVersionUID = 1L; // OK, enough is enough, what is it? Tell me now.
	
	private MessengerFrame view;
	
	private JTextField username;
	private JTextField password;	
	private JPanel loggingInPanel;
	private JPanel waitingPanel;
	
	public AlternateLoginPane(MessengerFrame view) {
		super();
		
		
		this.view = view;
		//setLayout(new GridLayout(4, 1));
		setPreferredSize(new Dimension(450,310));
		view.setResizable(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
		
		loggingInPanel = new JPanel();
		loggingInPanel.setLayout(new BoxLayout(loggingInPanel, BoxLayout.Y_AXIS));
		
		JPanel card = new JPanel();
		JLabel lblSignUp = new JLabel("Sign in");
		lblSignUp.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblSignUp.setBounds(34, 25, 61, 16);
		card.add(lblSignUp);
		card.setBackground(Color.white);
		loggingInPanel.add(card);
		
		JPanel card4 = new JPanel();
		JLabel Info = new JLabel("Use your Notsapp account.");
		Info.setBounds(34, 25, 61, 16);
		card4.add(Info);
		card4.setBackground(Color.white);
		loggingInPanel.add(card4);
		
		JPanel card5 = new JPanel();
		card5.setBackground(Color.white);
		JButton infoButton = new JButton("What's this?");
		infoButton.setBorderPainted(false);
		infoButton.setBackground(Color.white);
		infoButton.setForeground(Color.GRAY);
		infoButton.setFont(new Font("Ariel", Font.BOLD, 12));
		infoButton.addActionListener(e -> JOptionPane.showMessageDialog(view,
				"This is a chat service!",
			    "Information",
			    JOptionPane.INFORMATION_MESSAGE));
		//infoButton.setBounds(157, 192, 91, 29);
		card5.add(infoButton);
		loggingInPanel.add(card5);
		
		JPanel card0 = new JPanel();		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(34, 73, 79, 16);
		
		card0.add(lblUsername);
		
		username = new JTextField();
		username.setBounds(118, 68, 130, 26);
		username.setText("justin_trudeau");
		username.setColumns(21);
		card0.setBackground(Color.white);
		card0.add(username);
		
		loggingInPanel.add(card0);
		
		JPanel card1 = new JPanel();
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 114, 79, 16);
		card1.setBackground(Color.white);
		card1.add(lblPassword);
		
		password = new JPasswordField();
		password.setText("trump1sStup1d");
		password.setColumns(21);
		password.setBounds(118, 109, 130, 26);

		card1.add(password);
		
		loggingInPanel.add(card1);
		
		JPanel card2 = new JPanel();	
		
		card2.setBackground(Color.white);
		JButton loginButton = new JButton("Login");
		loginButton.setBackground(Color.blue);
		loginButton.setPreferredSize(new Dimension(300, 35));
		loginButton.setForeground(Color.white);
		loginButton.setFont(new Font("Ariel", Font.BOLD, 13));
		loginButton.addActionListener(e -> view.login(username.getText(), password.getText()));
		loginButton.setBounds(34, 172, 91, 29);
		
		card2.add(loginButton);
		loggingInPanel.add(card2);
		
		JPanel card3 = new JPanel();
		card3.setBackground(Color.white);
		JLabel lblNewuser = new JLabel("No account?");
		//lblUsername.setBounds(117, 192, 91, 29);
		card3.add(lblNewuser);
		JButton createUserButton = new JButton("Create One!");
		createUserButton.setBackground(Color.white);
		createUserButton.setBorderPainted(false);
		createUserButton.setForeground(Color.blue);
		createUserButton.setFont(new Font("Ariel", Font.BOLD, 12));
		createUserButton.addActionListener(e -> new AlternateCreateUserDialog(view));
		createUserButton.setBounds(157, 192, 91, 29);
		card3.add(createUserButton);
		loggingInPanel.add(card3);
		
		add(loggingInPanel);
		
		waitingPanel = new JPanel();
		waitingPanel.setLayout(new BorderLayout());
		waitingPanel.setBackground(Color.white);
		JLabel loading = loadingPanel();
		JLabel label = new JLabel("Please Wait..");
		waitingPanel.add(label, BorderLayout.SOUTH);
		waitingPanel.add(loading, BorderLayout.CENTER);
		//waitingPanel.add(new JLabel("Please wait..."));
		//loggingInPanel.add(waitingPanel);
	}
	
	public void setWaiting() {
		remove(loggingInPanel);
		add(waitingPanel);
	}
	
	// If login is unsuccessful
	public void restartLogin() {
		
		// Display failed message
		/*
		remove(waitingPanel);
		add(loggingInPanel);
		
		if(username.equals("") || password.equals("")){
			JOptionPane.showMessageDialog(view,
					"Both username and password can not be empty!",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}
		
		else if(username.equals("username") || password.equals("password")){
			JOptionPane.showMessageDialog(view,
					"Please type in your password and username!",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}
		else if(username.equals("Login failed. Please reenter username.") || password.equals("myFailedLoginPassword")){
			JOptionPane.showMessageDialog(view,
				    "Stop clicking the button",
				    "Sign-in Error",
				    JOptionPane.WARNING_MESSAGE);
		}
		else{ 
			JOptionPane.showMessageDialog(view,
				    "Login was unsuccesful, please try again",
				    "Sign-in Error",
				    JOptionPane.WARNING_MESSAGE);
		}
		
		username.setText("Login failed. Try Again.");
		password.setText("LoginPassword");*/
		
		remove(waitingPanel);
		add(loggingInPanel);
		
		
		JOptionPane.showMessageDialog(view,
				"Login was unsuccesful",
				"Sign-in Error",
				JOptionPane.WARNING_MESSAGE);
		
		
		username.setText("");
		password.setText("");
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
       java.net.URL imgURL = LoginPane.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
	private JLabel loadingPanel() {
		
		JLabel picture = new JLabel();
		JPanel panel = new JPanel();
	    BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	    panel.setLayout(layoutMgr);
	    
	    ImageIcon icon = createImageIcon("images/"+ "loading" + ".gif");
        System.out.println(icon);
        picture.setIcon(icon);
        if  (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
        
        panel.add(picture);
	    return picture;
    
	}
	
	public void displayMessage(String message) {
		username.setText(message);
	}
	
}
