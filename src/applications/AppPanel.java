package applications;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AppPanel extends JPanel implements Observer {
	
	private static final long serialVersionUID = 1L; // WTF IS THIS?!
	
	protected App model;
	
	private boolean configured;
	
	public AppPanel(App model) {
		super();
		
		this.model = model;
		
		configured = false;
		
		model.addObserver(this);
		
		
		setLayout(new GridLayout(1, 1));
		
		JLabel label = new JLabel("Waiting for history.");
		
		add(label);
		
		checkConfigure();
	}
	
	@Override
	public final void update(Observable o, Object arg) {
		//System.out.println("update() CID:" + this.getChatID() + " target:" + this.getAppTargetID()); 
		checkConfigure();
		
		this.updatePanel(arg);
	}
	
	private synchronized void checkConfigure() {
		if(!configured) {
			//System.out.println("Not configured. CID:" + this.getChatID() + " target:" + this.getAppTargetID());
			if(model.isConfigured()) {
				this.configured = true;
				this.configure();
				//System.out.println("Model is configured.");
			}
		}
	}
	
	public abstract void updatePanel(Object arg);
	
	public abstract void configure();
	
	public final int getChatID() {
		return model.getChatID();
	}

	public final int getAppTargetID() {
		return model.getTargetID();
	}
	
	public final String getAppName() {
		return model.getName();
	}
	
	public final String getTitle() {
		return model.getTitle();
	}
	
	public boolean equals(Object o) {
		AppPanel other = null;
		
		try {
			other = (AppPanel)o;
		} catch (ClassCastException e) {
			return false;
		}
		
		return this.getChatID() == other.getChatID() && this.getAppTargetID() == other.getAppTargetID();
	}
	
}
