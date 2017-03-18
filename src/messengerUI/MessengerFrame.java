package messengerUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import applications.App;
import applications.chat.ChatApp;
import communications.User;
import messenger.AppModel;
import messenger.ProgramController;

public final class MessengerFrame extends JFrame implements Observer {
	 
	private static final long serialVersionUID = 1L;

	private ProgramController controller;
	
	private AppModel model;
	
	private boolean loggedIn;
	
	private LoginPane loginPane;
	private MessengerPane messengerPane;
	private AppsPane appsPane;
	
	private boolean appsPaneOpen;
	
	public MessengerFrame(ProgramController controller) {
		this.controller = controller;
		
		loggedIn = false;
		appsPaneOpen = false;
		
		// loginPanel = new LoginPanel();
		
		// If this does extend JFrame is can make itself visible (and set exit on close etc. at beginning)
		// setVisible(true);
		
		
		
		//setSize(400, 400);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setPreferredSize(new Dimension(500,800));
	    
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent event) {
	        	exitProcedure();
	        }
	    });
		
		setLayout(new GridLayout(1, 1));
		
		loginPane = new LoginPane(this);
		
		add(loginPane);
		
		pack();
		setTitle("NotsApp");
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void requestFriend(String username) {
		controller.requestFriend(username);
	}
	
	public void createChat(List<User> participants) {
		model.startChat(participants);
	}
	
	public void createUser(String username, String password) {
		
		System.out.println("In MessengerFrame: Attempt to create user with username " + username + " and password " + password + ".");
		
		controller.createUser(username, password);
		
		loginPane.setWaiting();
		revalidate();
		repaint();
	}
	
	public void editUser(String password, boolean[] edited, String[] fields) {
		controller.editUser(password, edited, fields);
	}
	
	public void login(String username, String password) {
		
		System.out.println("In MessengerFrame: Attempt to login with username " + username + " and password " + password + ".");
		
		controller.login(username, password);
		
		loginPane.setWaiting();
		revalidate();
		repaint();
	}
	
	public void onLogin(boolean success) {
		if(success) {
			loggedIn = true;
			
			System.out.println("In MessengerFrame: Login successful.");
			
			// Delete login pane...
			// loginPane = null;
			// ... and create chat/app panels
			// messengerPane = new MessengerPane();
			// tabbedAppsPane = new TabbedAppsPane();
			// Begin with tabbedAppsPane 'minimised'/hidden - it will be opened
			// when first app is opened.
			//			Can it be closed again if all apps are closed?
			
			
			messengerPane = new MessengerPane(model, this);
			appsPane = new AppsPane(this);
			
			remove(loginPane);
			
			setLayout(new GridLayout(1, 1));
			add(messengerPane);
			
			pack();
			revalidate();
		}
		else { // Login failed
			loggedIn = false;
			
			System.out.println("In MessengerFrame: Login failed.");
			
			loginPane.restartLogin();
			revalidate();
			repaint();
		}
	}
	
	public void userLogoff() {
		controller.logoff();
		
		GUILogoff();
	}
	
	public void GUILogoff() {
		if(loggedIn) {
			loggedIn = false;
			
			remove(messengerPane);
			remove(appsPane);
			appsPaneOpen = false;
			messengerPane = null;
			appsPane = null;
			setLayout(new GridLayout(1, 1));
			loginPane = new LoginPane(this);
			add(loginPane);
			pack();
			revalidate();
		}
	}
	
	public void exitProcedure() {
		dispose();
    	controller.logoff();
        System.exit(0);
	}
/*
	public void addChat(ChatApp chat) {
		messengerPane.addChat(chat);
		appsPane.openChat(chat.getChatID()); // Right place for this?
	}
*/
	public void openChat(ChatApp chat) {
		messengerPane.openChat(chat);
		appsPane.openChat(chat.getChatID());
	}
/*
	public void addApp(int chatID, App app) {
		appsPane.addApp(chatID, app);
	}
*/
	public void openApp(int chatID, App app) {
		if(!appsPaneOpen) {
			openAppsPane();
		}
		
		appsPane.openApp(chatID, app);
	}
	
	public void openAppsPane() {
		remove(messengerPane);

		setLayout(new GridLayout(1, 2));
		add(appsPane);
		add(messengerPane);

		pack();
		revalidate();
	}

	public void closeAppsPane() {
		remove(appsPane);
		remove(messengerPane);

		setLayout(new GridLayout(1, 1));
		add(messengerPane);

		pack();
		revalidate();
	}
	
	public void setModel(AppModel model) {
		this.model = model;
	}
	
	public void displayDialogue(String text) {
		JOptionPane.showMessageDialog(this, text);
	}
	
	public int getActiveChatID() {
		return model.getActiveChatID();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		if(arg == null) repaint();
		else if(ChatApp.class.isAssignableFrom(arg.getClass())) {
			// Argument is an App
			openChat((ChatApp)arg);
		}
		else if(App.class.isAssignableFrom(arg.getClass())) {
			// Argument is an App
			App app = (App)arg;
			openApp(app.getChatID(), app);
		}
		else if(arg.getClass().equals(String.class)) {
			// Argument is a String
			displayDialogue((String)arg);
		}
		else if(arg.getClass().equals(Integer.class)) {
			int chatID = ((Integer)arg).intValue();
			messengerPane.deleteChat(chatID);
			appsPane.deleteChat(chatID);
		}
	}
	
	@Override
	public void repaint() {
		super.repaint();
		
		if(messengerPane != null) {
			messengerPane.updateChatList();
			messengerPane.repaint();
		}
		if(appsPane != null) {
			appsPane.repaint();
		}
		
		Component[] comps = this.getComponents();
		for(Component comp : comps) comp.repaint();
	}
	
}
