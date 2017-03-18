package applications.test;

import java.awt.BorderLayout;
import javax.swing.JLabel;

import applications.App;
import applications.AppPanel;

public class TestPanel extends AppPanel {
	
	private static final long serialVersionUID = 1L;
	
	public TestPanel(App model) {
		super(model);
	}

	@Override
	public void configure() {
		
		removeAll();
		
		setLayout(new BorderLayout());
		
		JLabel text = new JLabel("This is a test app.");
		
		add(text, BorderLayout.CENTER);
		
		revalidate();
	}

	@Override
	public void updatePanel(Object arg) {
		// Does nothing
	}


	
}
