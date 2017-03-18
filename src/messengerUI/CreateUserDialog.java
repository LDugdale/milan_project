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
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class CreateUserDialog extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = -5791217417299917920L;
    
    MessengerFrame frame;
    
    //JTextArea usernamePane;
    //JTextArea passwordPane;
    JTextField usernamePane;
    JTextField passwordPane;
    JTextField confirmPasswordPane;

    public CreateUserDialog(MessengerFrame frame) {
        super(frame, "Create New User", true);
        
        if (frame != null) {
            Dimension parentSize = frame.getSize();
            Point p = frame.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        
        this.frame = frame;
        setPreferredSize(new Dimension(470,300));
        
        //getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        JPanel card = new JPanel();
        JLabel lblSignUp = new JLabel("Create an account");
        lblSignUp.setFont(new Font("Ariel", Font.PLAIN, 28));
        lblSignUp.setBounds(2, 25, 61, 16);
        lblSignUp.setForeground(Color.WHITE);
        card.add(lblSignUp);
        card.setBackground(new Color(51, 102, 153));
        getContentPane().add(card);
        
        JPanel username = new JPanel();
        //username.setLayout(new GridLayout(1,2));
        //username.add(new JLabel("Username: "));
        JLabel usernameLabel = new JLabel("                Username: ");
        usernameLabel.setForeground(Color.GRAY);
        username.add(usernameLabel);
        usernamePane = new JTextField();
        usernamePane.setText("");
        usernamePane.setColumns(21);
        usernamePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        username.setBackground(Color.white);
        username.setForeground(Color.GRAY);
        
        username.add(usernamePane);
        getContentPane().add(username);
        
        JPanel password = new JPanel();
        //password.setLayout(new GridLayout(1,2));
        JLabel passwordLabel = new JLabel("                Password: ");
        passwordLabel.setForeground(Color.GRAY);
        password.add(passwordLabel);
        
        passwordPane = new JPasswordField();
        passwordPane.setText("");
        passwordPane.setColumns(21);
        passwordPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        password.setBackground(Color.white);
        password.add(passwordPane);
        getContentPane().add(password);
        
        JPanel confirmPassword = new JPanel();
        //confirmPassword.setLayout(new GridLayout(1,3));
        JLabel lblconfirmPass = new JLabel("Confirm Password: ");
        confirmPassword.add(lblconfirmPass);
        lblconfirmPass.setForeground(Color.GRAY);
        
        confirmPasswordPane = new JPasswordField();
        confirmPasswordPane.setText("");
        confirmPasswordPane.setColumns(21);
        confirmPasswordPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        confirmPasswordPane.setBackground(Color.white);
        confirmPassword.setBackground(Color.white);
        
        confirmPassword.add(confirmPasswordPane);
        
        getContentPane().add(confirmPassword);
        
        JPanel buttonPane = new JPanel();
        //buttonPane.setLayout(new GridLayout(1,2));
        
        JButton cancelButton = new JButton("Back");
        buttonPane.add(cancelButton);
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setPreferredSize(new Dimension(150, 35));
        cancelButton.setForeground(Color.white);
        cancelButton.setFont(new Font("Ariel", Font.BOLD, 13));
        
        JButton button = new JButton("Next");
        
        button.setBackground(new Color(51, 102, 153));
        button.setPreferredSize(new Dimension(150, 35));
        button.setForeground(Color.white);
        button.setFont(new Font("Ariel", Font.BOLD, 13));
        button.addActionListener(this);
        buttonPane.add(button);
        buttonPane.setBackground(Color.white);
        
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
        System.out.println("Username: " + usernamePane.getText() + ", Password: " + passwordPane.getText());
        
        if(usernamePane.getText() != ""){ // ideally we want a list of users to check against, not sure were it is
            if (passwordPane.getText().equals(confirmPasswordPane.getText())){
                if (8 > passwordPane.getText().length()  || passwordPane.getText().length()  > 32){
                    JOptionPane.showMessageDialog(frame,
                            "make sure you password is between 8 and 32 characters long!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                else{
                    frame.createUser(usernamePane.getText(), passwordPane.getText());
                    JOptionPane.showMessageDialog(frame,
                            "Welcome to Notsapp!",
                            "Infomation",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    setVisible(false);
                    dispose();
                    
                }
                
            }
            else if(passwordPane.getText().equals("") || confirmPasswordPane.getText().equals("") || usernamePane.getText().equals("")){
                JOptionPane.showMessageDialog(frame,
                        "Both username and password can not be empty!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(frame,
                        "Passwords do not match please try again!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(5 > usernamePane.getText().length() || usernamePane.getText().length() > 20){
            JOptionPane.showMessageDialog(frame,
                    "Make sure you username is between 5 and 20 characters !",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(frame,
                    "This username already exsits, please choose another!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        
        //setVisible(false);
        //dispose();
    }/*
        System.out.println("Username: " + usernamePane.getText() + ", Password: " + passwordPane.getText());
        
        frame.createUser(usernamePane.getText(), passwordPane.getText());
        
        setVisible(false);
        dispose();*/
    
}