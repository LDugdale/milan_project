package messengerUI;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JTabbedPane;

import applications.App;
import applications.AppPanel;

public final class AppsPane extends JTabbedPane {
	
	private static final long serialVersionUID = 1L; // Make it stop.
	
	private MessengerFrame view;
	
	private Map<Integer, TreeMap<Integer, AppPanel>> apps; // chatID, then targetID
	
	public AppsPane(MessengerFrame view) {
		super();

		this.view = view;
		
		apps = new TreeMap<Integer, TreeMap<Integer, AppPanel>>();
		
		// Holds tabs with apps associated with the
		// currently open chat.
	}
	
	public void openApp(int chatID, App app) {
		// If the app set for this chat has not been initialised...
		if (!apps.containsKey(chatID)) {
			apps.put(chatID, new TreeMap<Integer, AppPanel>());
		}

		// If the app is not present in the apps list, add it and add it to the tabbed panes
		if (apps.get(chatID).put(app.getTargetID(), app.getPanel()) == null) {
			add(app.getTitle(), apps.get(chatID).get(app.getTargetID()));
		}
		
		try {
			setSelectedComponent(apps.get(chatID).get(app.getTargetID()));
		} catch (IllegalArgumentException e) {
			System.out.println("App not available to open.");
		}
	}
	
	public void openChat(int chatID) {
		removeAll();
		
		if(apps.containsKey(chatID)) {
			if(apps.get(chatID).size() > 0) {
				for( AppPanel app : apps.get(chatID).values() ) {
					add(app.getTitle(), app);
				}
				view.openAppsPane();
			}
			else {
				view.closeAppsPane();
			}
		}
		else {
			view.closeAppsPane();
		}
	}
	
	// Should never be open chat
	public void deleteChat(int chatID) {
		apps.remove(chatID);
	}
}
