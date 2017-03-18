package applications.test;

import applications.App;
import communications.History;
import communications.Message;
import messenger.AppModel;

public class TestApp extends App {
	
	public TestApp() {
		super();
	}

	public TestApp(AppModel model, Integer myChatID) {
		super(model, myChatID);
	}

	public void updateHistory() {
		// Does nothing
	}

	public boolean receiveMessage(Message message) {
		return true;
	}

	public boolean receiveHistory(History history) {
		//view.configure();
		notifyObservers(); // What kind of object to send to configure view?
		return true;
	}
	
	public String getTitle() {
		return "My Test App";
	}

}
