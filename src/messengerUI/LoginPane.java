package messengerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPane extends JPanel {
	
	private static final long serialVersionUID = 1L; // OK, enough is enough, what is it? Tell me now.
	
	private MessengerFrame view;
	
	private JTextField username;
	private JTextField password;	
	private JPanel loggingInPanel;
	private JPanel waitingPanel;
	
	public LoginPane(MessengerFrame view) {
		super();
		
		
		this.view = view;
		//setLayout(new GridLayout(4, 1));
		view.setPreferredSize(new Dimension(450,310));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
		
		loggingInPanel = new JPanel();
		loggingInPanel.setLayout(new BoxLayout(loggingInPanel, BoxLayout.Y_AXIS));
		
		JPanel card = new JPanel();
		JLabel lblSignUp = new JLabel("Sign in");
		lblSignUp.setFont(new Font("Ariel", Font.PLAIN, 28));
		lblSignUp.setForeground(Color.WHITE);
		lblSignUp.setBounds(34, 25, 61, 16);
		card.add(lblSignUp);
		card.setBackground(new Color(51, 102, 153));
		loggingInPanel.add(card);
		
		JPanel card4 = new JPanel();
		JLabel Info = new JLabel("Use your NotsApp account.");
		Info.setBounds(34, 25, 61, 16);
		card4.add(Info);
		card4.setBackground(Color.white);
		Info.setForeground(Color.GRAY);
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
		lblUsername.setForeground(Color.GRAY);
		card0.add(lblUsername);
		
		username = new JTextField();
		username.setBounds(118, 68, 130, 26);
		username.setText("");
		username.setColumns(21);
		card0.setBackground(Color.white);
		card0.add(username);
		
		loggingInPanel.add(card0);
		
		JPanel card1 = new JPanel();
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 114, 79, 16);
		lblPassword.setForeground(Color.GRAY);
		card1.setBackground(Color.white);
		card1.add(lblPassword);
		
		password = new JPasswordField();
		password.setText("");
		password.setColumns(21);
		password.setBounds(118, 109, 130, 26);

		card1.add(password);
		
		loggingInPanel.add(card1);
		
		JPanel card2 = new JPanel();	
		
		card2.setBackground(Color.white);
		JButton loginButton = new JButton("Login");
		loginButton.setBackground(new Color(51, 102, 153));
		loginButton.setPreferredSize(new Dimension(300, 35));
		loginButton.setForeground(Color.white);
		loginButton.setFont(new Font("Ariel", Font.BOLD, 13));
		loginButton.addActionListener(e -> view.login(username.getText(), password.getText()));
		loginButton.setBounds(34, 172, 91, 29);

		// virtually clicks the sendButton if enter is pressed.
		password.addKeyListener(new KeyListener(){			

			public void keyPressed(KeyEvent e) {
			    if(e.getKeyCode() == KeyEvent.VK_ENTER){
			        e.consume();
			        loginButton.doClick();
		        }
			}
			
			// unused interface methods
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}

		});
		
		card2.add(loginButton);
		loggingInPanel.add(card2);
		
		JPanel card3 = new JPanel();
		card3.setBackground(Color.white);
		JLabel lblNewuser = new JLabel("No account?");
		lblUsername.setBounds(117, 192, 91, 29);
		lblNewuser.setForeground(Color.GRAY);
		card3.add(lblNewuser);
		JButton createUserButton = new JButton("Create One Here!");
		createUserButton.setBackground(Color.white);
		createUserButton.setBorderPainted(false);
		createUserButton.setForeground(Color.GRAY);
		createUserButton.setFont(new Font("Ariel", Font.BOLD, 12));
		createUserButton.addActionListener(e -> new CreateUserDialog(view));
		createUserButton.setBounds(157, 192, 91, 29);
		card3.add(createUserButton);
		loggingInPanel.add(card3);
		
		add(loggingInPanel);
		
		waitingPanel = new JPanel();
		//waitingPanel.setLayout(new BorderLayout());
		//waitingPanel.add(loadingPanel());
		waitingPanel.setBackground(Color.WHITE);
		waitingPanel.add(new JLabel("Please wait..."));
		//loggingInPanel.add(waitingPanel);
	}
	
	public void setWaiting() {
		remove(loggingInPanel);
		add(waitingPanel);
	}
	
	// If login is unsuccessful
	public void restartLogin() {
		
		// Display failed message
		
		remove(waitingPanel);
		add(loggingInPanel);
		
		
		JOptionPane.showMessageDialog(view,
				"Login was unsuccesful",
				"Sign-in Error",
				JOptionPane.WARNING_MESSAGE);
		
		
		username.setText("");
		password.setText("");
	}
	
	private JPanel loadingPanel() {
		
		JPanel panel = new JPanel();/*
	    BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	    panel.setLayout(layoutMgr);

	    ClassLoader cldr = this.getClass().getClassLoader();
   
		URL imageURL   = cldr.getResource("torgoen.com/skin/frontend/base/default/images/loader.gif");
	    ImageIcon imageIcon = new ImageIcon(imageURL);
	    JLabel iconLabel = new JLabel();
	    iconLabel.setIcon(imageIcon);
	    imageIcon.setImageObserver(iconLabel);*/
		URL url;
		try {
			url = new URL("https://www.torgoen.com/skin/frontend/base/default/images/loader.gif");
			BufferedImage image = ImageIO.read(url);

			ImageIcon icon = new ImageIcon(image); 

			JLabel picture = new JLabel();
			picture.setIcon(icon);
			 JLabel label = new JLabel("Loading...");
			    panel.add(picture);
			    panel.add(label);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	   
	    return panel;
	    
	}
	
	public void displayMessage(String message) {
		username.setText(message);
	}
	
}
