package applications.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import applications.App;
import applications.AppPanel;
import messengerUI.MessengerPane;

public final class ChatPanel extends AppPanel {
	
	private static final long serialVersionUID = 1L; // Seriously, WTF is this thing?
	
	private MessengerPane pane;
	
	private JTextArea messageField;
	private JTextArea histortText;
	
	private JPanel sendingPanel;
	
	public ChatPanel(App model) {
		super(model);
	}
	
	@Override
	public void configure() {
		
		//System.out.println("ChatPanel configure().");
		
		removeAll();
		
		
		setLayout(new BorderLayout());
		
		
		JPanel feed = new JPanel();
		//feed.setLayout(new GridLayout(2, 1));
		feed.setLayout(new BoxLayout(feed, BoxLayout.Y_AXIS));
		
		histortText = new JTextArea(10, 30);
		histortText.setText(""); // Better suggestion?
		histortText.setLineWrap(true);
		histortText.setWrapStyleWord(true);
		histortText.setEditable(false);
		DefaultCaret caret = (DefaultCaret) histortText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane historyPane = new JScrollPane(histortText);
		
		//feed.add(historyPane, BorderLayout.SOUTH);
		
		JPanel send = new JPanel();
		send.setLayout(new GridLayout(1,1));
		sendingPanel = new JPanel();
		//sendingPanel.setPreferredSize(new Dimension(50,50));
		sendingPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 2.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        
		//sendingPanel.setLayout(new GridLayout(1, 2));
        JPanel TextPanel = new JPanel();
        TextPanel.setLayout(new GridLayout(1,1));
		messageField = new JTextArea(10, 30);
		messageField.setWrapStyleWord(true);
		messageField.setMargin(new Insets(5, 5, 5, 5));
		messageField.setLineWrap(true);

		DefaultCaret positon = (DefaultCaret) messageField.getCaret();
		positon.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//JScrollPane messagePane = new JScrollPane(messageField);
		//messageField.setPreferredSize(new Dimension(50,50));
		TextPanel.add(messageField);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBackground(Color.WHITE);
		JButton sendButton = new JButton("Send");
		sendButton.setBackground(new Color(51, 102, 153));
		sendButton.setForeground(Color.WHITE);
		sendButton.setPreferredSize(new Dimension(70, 35));
		sendButton.addActionListener(e -> {
				if(messageField.getText().length() > 0) {
					model.sendMessage("",messageField.getText()); // A chat message has an empty meta
					messageField.setText("");
				}
			});
		
		// virtually clicks the sendButton if enter is pressed.
		messageField.addKeyListener(new KeyListener(){			

			public void keyPressed(KeyEvent e) {
			    if(e.getKeyCode() == KeyEvent.VK_ENTER){
			        e.consume();
			        sendButton.doClick();
		        }
			}
			
			// unused interface methods
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}

		});
		
		//TextPanel.add(messageField);
		buttonPanel.add(sendButton, BorderLayout.EAST);
		//TextPanel.add(messageField);
		//TextPanel.setPreferredSize(new Dimension(70, 35));
		
		sendingPanel.add(buttonPanel, gbc);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.2;
		gbc.gridy=0;
        gbc.gridx = 0;
        sendingPanel.add(TextPanel, gbc);
		
        //TextPanel.setMinimumSize(new Dimension(200, 50));
        
		//feed.add(sendingPanel);
        
        //Dimension textMinimumSize = new Dimension(100, 50);
        //Dimension buttonMinimumSize = new Dimension(70, 35);
        //TextPanel.setMinimumSize(new Dimension(200, 50));
        //buttonPanel.setMinimumSize(buttonMinimumSize);
        Dimension minimumSize = new Dimension(40, 40);
        buttonPanel.setMinimumSize(minimumSize);
        TextPanel.setMinimumSize(minimumSize);
        //TextPanel.setPreferredSize(new Dimension(70, 35));
		send.add(sendingPanel);
		//Create a split pane with the two scroll panes in it.
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		historyPane,send);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(true);
        split.setResizeWeight(1f);
        split.setDividerLocation(280);

        
		
		add(split, BorderLayout.CENTER);
		
		updateHistory(); // Write history to historyText
		
	}
	
	public void updateHistory() {
		String history = new String();
		List<String> chatHistory = ((ChatApp)model).getChatHistory();
		for(int i=0; i<chatHistory.size(); i++) {
			history += chatHistory.get(i) + "\n";
		}
		
		histortText.setText(history);
		updateTitle();
		
		revalidate();
		repaint();
	}
	
	public void setMessengerPane(MessengerPane pane) {
		this.pane = pane;
	}
	
	public void updateTitle() {
		if(pane != null) pane.updateChatTitle((ChatApp)model);
	}

	@Override
	public void updatePanel(Object arg) {
		updateHistory();
	}
	
	public ChatApp getModel() {
		return (ChatApp)model;
	}
	
}
