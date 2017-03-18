package applications.calendar;

import applications.App;
import communications.History;
import communications.Message;
import messenger.AppModel;

public class CalendarApp extends App {
	
	Calendar calendar;
	
	public CalendarApp() {
		super();
	}
	
	public CalendarApp(AppModel model, Integer myChatID) {
		super(model, myChatID);
		
		this.calendar = new Calendar();
	}

	@Override
	public void updateHistory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean receiveMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean receiveHistory(History history) {
		// TODO Auto-generated method stub
		return false;
	}

}
