package messengerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import applications.App;
import applications.AppUtilities;
import applications.chat.ChatApp;
import applications.chat.ChatPanel;
import communications.ChatInfo;
import communications.User;
import messenger.AppModel;

public final class MessengerPane extends JPanel {
	
	private static final long serialVersionUID = 1L; // Does this need changing?
	
	MessengerFrame view;
	
	AppModel model;
	
	Map<Integer, ChatPanel> chatPanels; // Key is chatID
	
	JTabbedPane chats; // Will be generalised to TabbedChatPane
	
	JPanel chatList;
	JPanel friendList;
	
	public MessengerPane(AppModel model, MessengerFrame view) {
		super();
		
		this.model = model;
		this.view = view;
		
		view.setPreferredSize(new Dimension(800, 400));
		this.chatPanels = new TreeMap<Integer, ChatPanel>();
		
		// This panel will organise all aspects of the messenger.
		// It will have a TabbedPane of ChatPanels, and the panel
		// for existing chats, and will maybe have a CardLayout
		// for switching between current chats and the window in 
		// which new chats are started.
		
		setLayout(new GridLayout(1, 2));
		
		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		
		chats = new JTabbedPane();
		
		
		//chats.add(model.getActiveChat().getTitle(), model.getActiveChat().getPanel());
		openChat(model.getActiveChat());
       
		
		chats.addChangeListener(e -> {
			try {
				model.openChat(((ChatPanel)chats.getSelectedComponent()).getChatID());
			}
			catch (Exception ex) {
				System.out.println("Problem opening chat from MessengerPane button.");
				//ex.printStackTrace();
			}
		});
		
		left.add(chats, BorderLayout.CENTER);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(51, 102, 153));
	
		
		JMenu utilMenu = new JMenu("Menu");
		utilMenu.setForeground(Color.WHITE);
		
		JMenuItem seeUserItem = new JMenuItem("My Profile");
		seeUserItem.addActionListener(e -> new UserInfoDialog(view, model.getUser()));
		utilMenu.add(seeUserItem);
		
		JMenuItem editUserItem = new JMenuItem("Edit User");
		editUserItem.addActionListener(e -> new EditUserDialog(view));
		utilMenu.add(editUserItem);
		
		JMenuItem logoffItem = new JMenuItem("Log Out");
		logoffItem.addActionListener(e -> view.userLogoff());
		utilMenu.add(logoffItem);
		
		menuBar.add(utilMenu);
		
		JMenu appMenu = new JMenu("Add App");
		appMenu.setForeground(Color.WHITE);
		Map<String, JMenu> subMenuListing = new TreeMap<String, JMenu>();
		for( Entry<Integer, Class<? extends App>> entry : AppUtilities.getAppTypes().entrySet() ) {
			try {
				App instance = entry.getValue().newInstance();
				JMenuItem menuItem = new JMenuItem(instance.getDisplayName());
				menuItem.addActionListener(e -> model.openApp(model.getActiveChatID(), entry.getKey()));
				//System.out.println("instance.getType() = " + instance.getType()); 
				if(instance.getType().length() < 1) appMenu.add(menuItem);
				else if(subMenuListing.get(instance.getType()) != null) {
					subMenuListing.get(instance.getType()).add(menuItem);
				}
				else {
					subMenuListing.put(instance.getType(), new JMenu(instance.getType()));
					subMenuListing.get(instance.getType()).add(menuItem);
				}
			}
			catch (InstantiationException | IllegalAccessException e1) {
				System.out.println("Error in App Menu building: " + e1.getMessage());
				e1.printStackTrace();
			}
		}
		for(Entry<String, JMenu> entry : subMenuListing.entrySet()) {
			appMenu.add(entry.getValue());
		}
		
		menuBar.add(appMenu);
		
		left.add(menuBar, BorderLayout.PAGE_START);
		
		//add(left);
		
		
		chatList = new JPanel();
		updateChatList();
		
		JPanel chatsPane = new JPanel();
		JPanel Info = new JPanel();
		Info.setLayout(new GridLayout(1,2));
		chatsPane.setLayout(new BorderLayout());
		//chatsPane.setLayout(new GridLayout(1,9));
		JLabel lblmessage = new JLabel("   Current chats");
		lblmessage.setFont(new Font("Ariel", Font.BOLD, 14));
		lblmessage.setForeground(Color.white);
		//Info.add(new JSeparator(SwingConstants.VERTICAL));
		JButton createChatButton = new JButton("Start New Chat");
		createChatButton.setFont(new Font("Ariel", Font.BOLD, 14));
		createChatButton.setPreferredSize(new Dimension(100, 40));
		//createChatButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		createChatButton.setForeground(Color.white);
		createChatButton.setBorder(BorderFactory.createMatteBorder(
                0, 2, 0, 0, Color.white));
		createChatButton.setBackground(new Color(51, 102, 153));
		createChatButton.addActionListener( e ->
			{
				new CreateChatDialog(view, model.getFriends());
			}	
		);
		
		
		Info.add(lblmessage, BorderLayout.WEST);		
		Info.setBackground(new Color(51, 102, 153));
		Info.add(createChatButton, BorderLayout.EAST);
		
		chatsPane.add(Info, BorderLayout.PAGE_START);
		
		JScrollPane chatsListPane = new JScrollPane(chatList);
		chatsPane.add(chatsListPane, BorderLayout.CENTER);
		
		
		JPanel friendsPane = new JPanel();
		friendsPane.setLayout(new BorderLayout());
		
		
		//JPanel chatsPane = new JPanel();
		JPanel friends = new JPanel();
		friends.setLayout(new GridLayout(1,2));
		//chatsPane.setLayout(new BorderLayout());
		//chatsPane.setLayout(new GridLayout(1,9));
		JLabel lblMyFriends = new JLabel("   My friends");
		lblMyFriends.setFont(new Font("Ariel", Font.BOLD, 14));
		lblMyFriends.setForeground(Color.white);
		//Info.add(new JSeparator(SwingConstants.VERTICAL));
		JButton addFriendButton = new JButton("Add Friend");
		addFriendButton.setFont(new Font("Ariel", Font.BOLD, 14));
		addFriendButton.setPreferredSize(new Dimension(100, 40));
		//createChatButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		addFriendButton.setForeground(Color.white);
		addFriendButton.setBorder(BorderFactory.createMatteBorder(
                0, 2, 0, 0, Color.white));
		addFriendButton.setBackground(new Color(51, 102, 153));
		
		//JButton addFriendButton = new JButton("Add Friend");
		addFriendButton.addActionListener( e ->
			{
				new AddFriendDialog(view);
			}	
		);
		
		friends.add(lblMyFriends, BorderLayout.WEST);		
		friends.setBackground(new Color(51, 102, 153));
		friends.add(addFriendButton, BorderLayout.EAST);
		friendsPane.add(friends, BorderLayout.PAGE_START);
		
		//friendsPane.add(addFriendButton, BorderLayout.PAGE_START);
		
		friendList = new JPanel();
		updateFriendList();
		JScrollPane friendsListPane = new JScrollPane(friendList);
		friendsPane.add(friendsListPane, BorderLayout.CENTER);
		
		JTabbedPane rightView = new JTabbedPane();
		rightView.add("Chats", chatsPane);
		rightView.add("Friends", friendsPane);
		
		rightView.setSelectedComponent(chatsPane);
		
		left.setMinimumSize(new Dimension(150, 50));
		rightView.setMinimumSize(new Dimension(280, 50));
		//Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   left,rightView);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        
        
        add(splitPane);
		//add(rightView);
	}
	
	public void updateChatList() {
		if(chatList != null) {
			chatList.removeAll();
			//chatList.setLayout(new BorderLayout());
			chatList.setLayout(new BoxLayout(chatList, BoxLayout.PAGE_AXIS));
			//chatList.setLayout(new GridLayout(9, 1));
			
			Set<ChatInfo> userChats = model.getUserChats();
			Map<Integer, ChatApp> openChats = model.getOpenChatsMap();
			ChatApp temp;
			
		
			for( ChatInfo info : userChats ) {
				String title = ((temp = openChats.get(info.getChatID()))!=null)?temp.getTitle():(ChatApp.generateTitle(model.getUsername(), info));
				String latestChat = "latest chat";//((temp2 = openChats.get(info.getChatID()))!=null)?temp.getChatHistory().get(temp.getChatHistory().size()-1):("");
				//List<String> recentChat = temp.getChatHistory();
				
				JPanel chatPanel = new JPanel();
				//chatPanel.setLayout(new GridLayout(1,2));
				chatPanel.setLayout(new GridBagLayout());
				JButton chatButton = new JButton();
				chatButton.setBackground(Color.white);
				chatButton.setRolloverEnabled(true);
				chatButton.addActionListener(e -> {
						//System.out.println("Chat " + cid + " button pressed.");
						model.openChat(info.getChatID());
					});
				
				
				// delete chat
				JButton deleteChat = new JButton();
				deleteChat.setBackground(new Color(233, 18, 18));
				
			    JPanel delete = new JPanel();
			    delete.setLayout(new BorderLayout());
			    delete.setBackground(new Color(233, 18, 18));
		        JLabel lblDelete = new JLabel("Delete");
		        lblDelete.setFont(new Font("Ariel", Font.BOLD, 13));
		        lblDelete.setForeground(Color.WHITE);
		        delete.add(lblDelete, BorderLayout.CENTER);
		        deleteChat.add(delete, BorderLayout.CENTER);
				
				deleteChat.addActionListener(e->{
					model.deleteChat(info.getChatID());
				});
				
				
				JPanel lastChat = new JPanel();
				lastChat.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		        gbc.fill = GridBagConstraints.BOTH;
		        gbc.weightx = 1;
		        gbc.weighty = 1.0;
		        gbc.gridx = 0;
		        gbc.gridy = 0;
		        
				lastChat.setBackground(Color.white);
				JPanel group = new JPanel();
				group.setBackground(Color.white);
				group.setLayout(new BorderLayout());
				JLabel chatMemebers = new JLabel(title);
				group.add(chatMemebers, BorderLayout.WEST);
				JPanel lastChatRecived = new JPanel(); 
				lastChatRecived.setLayout(new BorderLayout());
				lastChatRecived.setBackground(Color.white);
				JLabel chat = new JLabel("lastChat");
				chat.setForeground(Color.GRAY);
				lastChatRecived.add(chat, BorderLayout.WEST);
				
				lastChat.add(group);
				//lastChat.add(lastChatRecived);
				
				chatButton.add(lastChat);
				chatPanel.add(chatButton, gbc);
				gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		        gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 0.05;
				gbc.gridy=0;
		        gbc.gridx = 1;
				chatPanel.add(deleteChat,gbc);
				chatList.add(chatPanel);
			}
			
			chatList.revalidate();
			chatList.repaint();
		}
	}
	
	public void updateFriendList() {
		if(friendList != null) {
			friendList.removeAll();
			friendList.setLayout(new BoxLayout(friendList, BoxLayout.PAGE_AXIS));
			//chatList.setLayout(new GridLayout(1, 2));
			
			List<User> friends = model.getFriends();
			
			for( User friend : friends ) {
				String title = friend.getUsername();
				//JButton friendButton = new JButton(title);
				
				JPanel friendPanel = new JPanel();
				friendPanel.setLayout(new GridLayout(1,1));
				JButton friendButton = new JButton(title);
				friendButton.setBackground(Color.white);
				
				friendButton.addActionListener(e -> {
						new UserInfoDialog(view, friend);
					});
				friendPanel.add(friendButton);
				friendList.add(friendPanel);
			}
			
			friendList.revalidate();
			friendList.repaint();
		}
	}
	
	public void openChat(ChatApp chat) {
		
		if (chat != null) {
			if (!chatPanels.containsKey(chat.getChatID())) {
				chatPanels.put(chat.getChatID(), (ChatPanel) chat.getPanel());
				chats.addTab(chat.getTitle(), chatPanels.get(chat.getChatID()));
				chatPanels.get(chat.getChatID()).setMessengerPane(this);
			}
			try {
				chats.setSelectedComponent(chatPanels.get(chat.getChatID()));
			} catch (IllegalArgumentException e) {
				System.out.println("Chat not available to open.");
			} 
		}
		else {
			// Do nothing
		}
		
	}
	
	public void deleteChat(int chatID) {
		
		try {
			if(((ChatPanel)chats.getSelectedComponent()).getChatID() == chatID) {
				view.openChat(chatPanels.values().iterator().next().getModel());
			}
		}
		catch (Exception e) { }
		
		chats.remove(chatPanels.get(chatID));
		chatPanels.remove(chatID);
		
		updateChatList();
	}
	
	public void updateChatTitle(ChatApp chat) {
		//System.out.println("updateChatTitle() called with " + chat.getTitle());
		int index = -1;
		if((index = chats.indexOfComponent(chatPanels.get(chat.getChatID()))) != -1) {
			chats.setTitleAt(index, chat.getTitle());
		}
		updateChatList();
	}
	
	@Override
	public void repaint() {
		updateChatList();
		updateFriendList();
		
		super.repaint();
		
		Component[] comps = this.getComponents();
		for(Component comp : comps) comp.repaint();
	}
	
	/*
	 * package messengerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import applications.App;
import applications.AppUtilities;
import applications.chat.ChatApp;
import applications.chat.ChatPanel;
import communications.ChatInfo;
import communications.User;
import messenger.AppModel;

public final class MessengerPane extends JPanel {
	
	private static final long serialVersionUID = 1L; // Does this need changing?
	
	MessengerFrame view;
	
	AppModel model;
	
	Map<Integer, ChatPanel> chatPanels; // Key is chatID
	
	JTabbedPane chats; // Will be generalised to TabbedChatPane
	
	JPanel chatList;
	JPanel friendList;
	
	public MessengerPane(AppModel model, MessengerFrame view) {
		super();
		
		this.model = model;
		this.view = view;
		
		view.setPreferredSize(new Dimension(800, 400));
		this.chatPanels = new TreeMap<Integer, ChatPanel>();
		
		// This panel will organise all aspects of the messenger.
		// It will have a TabbedPane of ChatPanels, and the panel
		// for existing chats, and will maybe have a CardLayout
		// for switching between current chats and the window in 
		// which new chats are started.
		
		setLayout(new GridLayout(1, 2));
		
		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		
		chats = new JTabbedPane();
		
		
		//chats.add(model.getActiveChat().getTitle(), model.getActiveChat().getPanel());
		openChat(model.getActiveChat());
       
		
		chats.addChangeListener(e -> model.openChat(((ChatPanel)chats.getSelectedComponent()).getChatID()));
		
		left.add(chats, BorderLayout.CENTER);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(51, 102, 153));
	
		
		JMenu utilMenu = new JMenu("Menu");
		utilMenu.setForeground(Color.WHITE);
		
		JMenuItem seeUserItem = new JMenuItem("My Profile");
		seeUserItem.addActionListener(e -> new UserInfoDialog(view, model.getUser()));
		utilMenu.add(seeUserItem);
		
		JMenuItem editUserItem = new JMenuItem("Edit User");
		editUserItem.addActionListener(e -> new EditUserDialog(view));
		utilMenu.add(editUserItem);
		
		JMenuItem logoffItem = new JMenuItem("Log Out");
		logoffItem.addActionListener(e -> view.userLogoff());
		utilMenu.add(logoffItem);
		
		menuBar.add(utilMenu);
		
		JMenu appMenu = new JMenu("Add App");
		appMenu.setForeground(Color.WHITE);
		Map<String, JMenu> subMenuListing = new TreeMap<String, JMenu>();
		for( Entry<Integer, Class<? extends App>> entry : AppUtilities.getAppTypes().entrySet() ) {
			try {
				App instance = entry.getValue().newInstance();
				JMenuItem menuItem = new JMenuItem(instance.getDisplayName());
				menuItem.addActionListener(e -> model.openApp(model.getActiveChatID(), entry.getKey()));
				//System.out.println("instance.getType() = " + instance.getType()); 
				if(instance.getType().length() < 1) appMenu.add(menuItem);
				else if(subMenuListing.get(instance.getType()) != null) {
					subMenuListing.get(instance.getType()).add(menuItem);
				}
				else {
					subMenuListing.put(instance.getType(), new JMenu(instance.getType()));
					subMenuListing.get(instance.getType()).add(menuItem);
				}
			}
			catch (InstantiationException | IllegalAccessException e1) {
				System.out.println("Error in App Menu building: " + e1.getMessage());
				e1.printStackTrace();
			}
		}
		for(Entry<String, JMenu> entry : subMenuListing.entrySet()) {
			appMenu.add(entry.getValue());
		}
		
		menuBar.add(appMenu);
		
		left.add(menuBar, BorderLayout.PAGE_START);
		
		//add(left);
		
		
		chatList = new JPanel();
		updateChatList();
		
		JPanel chatsPane = new JPanel();
		JPanel Info = new JPanel();
		Info.setLayout(new GridLayout(1,2));
		chatsPane.setLayout(new BorderLayout());
		//chatsPane.setLayout(new GridLayout(1,9));
		JLabel lblmessage = new JLabel("   Current chats");
		lblmessage.setFont(new Font("Ariel", Font.BOLD, 14));
		lblmessage.setForeground(Color.white);
		//Info.add(new JSeparator(SwingConstants.VERTICAL));
		JButton createChatButton = new JButton("Start New Chat");
		createChatButton.setFont(new Font("Ariel", Font.BOLD, 14));
		createChatButton.setPreferredSize(new Dimension(100, 40));
		//createChatButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		createChatButton.setForeground(Color.white);
		createChatButton.setBorder(BorderFactory.createMatteBorder(
                0, 2, 0, 0, Color.white));
		createChatButton.setBackground(new Color(51, 102, 153));
		createChatButton.addActionListener( e ->
			{
				new CreateChatDialog(view, model.getFriends());
			}	
		);
		
		
		Info.add(lblmessage, BorderLayout.WEST);		
		Info.setBackground(new Color(51, 102, 153));
		Info.add(createChatButton, BorderLayout.EAST);
		
		chatsPane.add(Info, BorderLayout.PAGE_START);
		
		JScrollPane chatsListPane = new JScrollPane(chatList);
		chatsPane.add(chatsListPane, BorderLayout.CENTER);
		
		
		JPanel friendsPane = new JPanel();
		friendsPane.setLayout(new BorderLayout());
		
		
		//JPanel chatsPane = new JPanel();
		JPanel friends = new JPanel();
		friends.setLayout(new GridLayout(1,2));
		//chatsPane.setLayout(new BorderLayout());
		//chatsPane.setLayout(new GridLayout(1,9));
		JLabel lblMyFriends = new JLabel("   My friends");
		lblMyFriends.setFont(new Font("Ariel", Font.BOLD, 14));
		lblMyFriends.setForeground(Color.white);
		//Info.add(new JSeparator(SwingConstants.VERTICAL));
		JButton addFriendButton = new JButton("Add Friend");
		addFriendButton.setFont(new Font("Ariel", Font.BOLD, 14));
		addFriendButton.setPreferredSize(new Dimension(100, 40));
		//createChatButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		addFriendButton.setForeground(Color.white);
		addFriendButton.setBorder(BorderFactory.createMatteBorder(
                0, 2, 0, 0, Color.white));
		addFriendButton.setBackground(new Color(51, 102, 153));
		
		//JButton addFriendButton = new JButton("Add Friend");
		addFriendButton.addActionListener( e ->
			{
				new AddFriendDialog(view);
			}	
		);
		
		friends.add(lblMyFriends, BorderLayout.WEST);		
		friends.setBackground(new Color(51, 102, 153));
		friends.add(addFriendButton, BorderLayout.EAST);
		friendsPane.add(friends, BorderLayout.PAGE_START);
		
		//friendsPane.add(addFriendButton, BorderLayout.PAGE_START);
		
		friendList = new JPanel();
		updateFriendList();
		JScrollPane friendsListPane = new JScrollPane(friendList);
		friendsPane.add(friendsListPane, BorderLayout.CENTER);
		
		JTabbedPane rightView = new JTabbedPane();
		rightView.add("Chats", chatsPane);
		rightView.add("Friends", friendsPane);
		
		rightView.setSelectedComponent(chatsPane);
		
		left.setMinimumSize(new Dimension(150, 50));
		rightView.setMinimumSize(new Dimension(280, 50));
		//Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   left,rightView);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        
        
        add(splitPane);
		//add(rightView);
	}
	
	public void updateChatList() {
		if(chatList != null) {
			chatList.removeAll();
			//chatList.setLayout(new BorderLayout());
			chatList.setLayout(new BoxLayout(chatList, BoxLayout.PAGE_AXIS));
			//chatList.setLayout(new GridLayout(9, 1));
			
			Set<ChatInfo> userChats = model.getUserChats();
			Map<Integer, ChatApp> openChats = model.getOpenChatsMap();
			ChatApp temp;
			
		
			for( ChatInfo info : userChats ) {
				String title = ((temp = openChats.get(info.getChatID()))!=null)?temp.getTitle():(ChatApp.generateTitle(model.getUsername(), info));
				String latestChat = "latest chat";//((temp2 = openChats.get(info.getChatID()))!=null)?temp.getChatHistory().get(temp.getChatHistory().size()-1):("");
				//List<String> recentChat = temp.getChatHistory();
				
				JPanel chatPanel = new JPanel();
				chatPanel.setLayout(new GridLayout(1,1));
				JButton chatButton = new JButton();
				chatButton.setBackground(Color.white);
				chatButton.setRolloverEnabled(true);
				chatButton.addActionListener(e -> {
						//System.out.println("Chat " + cid + " button pressed.");
						model.openChat(info.getChatID());
					});
				JPanel lastChat = new JPanel();
				lastChat.setLayout(new GridLayout(2,1));
				lastChat.setBackground(Color.white);
				JPanel group = new JPanel();
				group.setBackground(Color.white);
				group.setLayout(new BorderLayout());
				JLabel chatMemebers = new JLabel(title);
				group.add(chatMemebers, BorderLayout.WEST);
				JPanel lastChatRecived = new JPanel(); 
				lastChatRecived.setLayout(new BorderLayout());
				lastChatRecived.setBackground(Color.white);
				JLabel chat = new JLabel("lastChat");
				chat.setForeground(Color.GRAY);
				lastChatRecived.add(chat, BorderLayout.WEST);
				
				lastChat.add(group);
				//lastChat.add(lastChatRecived);
				
				chatButton.add(lastChat);
				chatPanel.add(chatButton);
				chatList.add(chatPanel);
			}
			
			chatList.revalidate();
			chatList.repaint();
		}
	}
	
	public void updateFriendList() {
		if(friendList != null) {
			friendList.removeAll();
			friendList.setLayout(new BoxLayout(friendList, BoxLayout.PAGE_AXIS));
			//chatList.setLayout(new GridLayout(1, 2));
			
			List<User> friends = model.getFriends();
			
			for( User friend : friends ) {
				String title = friend.getUsername();
				//JButton friendButton = new JButton(title);
				
				JPanel friendPanel = new JPanel();
				friendPanel.setLayout(new GridLayout(1,1));
				JButton friendButton = new JButton(title);
				friendButton.setBackground(Color.white);
				
				friendButton.addActionListener(e -> {
						new UserInfoDialog(view, friend);
					});
				friendPanel.add(friendButton);
				friendList.add(friendPanel);
			}
			
			friendList.revalidate();
			friendList.repaint();
		}
	}
	
	public void openChat(ChatApp chat) {
		
		if (chat != null) {
			if (!chatPanels.containsKey(chat.getChatID())) {
				chatPanels.put(chat.getChatID(), (ChatPanel) chat.getPanel());
				chats.addTab(chat.getTitle(), chatPanels.get(chat.getChatID()));
				chatPanels.get(chat.getChatID()).setMessengerPane(this);
			}
			try {
				chats.setSelectedComponent(chatPanels.get(chat.getChatID()));
			} catch (IllegalArgumentException e) {
				System.out.println("Chat not available to open.");
			} 
		}
		else {
			// Do nothing
		}
		
	}
	
	public void updateChatTitle(ChatApp chat) {
		//System.out.println("updateChatTitle() called with " + chat.getTitle());
		int index = -1;
		if((index = chats.indexOfComponent(chatPanels.get(chat.getChatID()))) != -1) {
			chats.setTitleAt(index, chat.getTitle());
		}
		updateChatList();
	}
	
	@Override
	public void repaint() {
		updateChatList();
		updateFriendList();
		
		super.repaint();
		
		Component[] comps = this.getComponents();
		for(Component comp : comps) comp.repaint();
	}
	
}

	 */
	
}
