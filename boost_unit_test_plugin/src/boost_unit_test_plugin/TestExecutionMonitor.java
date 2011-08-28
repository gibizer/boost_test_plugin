package boost_unit_test_plugin;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;

public class TestExecutionMonitor implements IConsoleListener, IDocumentListener {
	
	public static final String CONSOLE_TYPE = "org.eclipse.debug.ui.ProcessConsoleType";
	private IConsoleManager mgr;
	private TestExecutionListener listener;
	
	
	public TestExecutionMonitor(IConsoleManager mgr){
		this.mgr = mgr;
	}
	
	public void addExecutionListener(TestExecutionListener listener){
		this.listener = listener;
	}
	
	public void start()
	{
		mgr.addConsoleListener(this);
	}

	@Override
	public void consolesAdded(IConsole[] consoles) {
		for (IConsole console : consoles){
			if (CONSOLE_TYPE.equals(console.getType())
					&& console.getName().contains("[C/C++ Application]")){
				TextConsole textConsole = (TextConsole)console;
				textConsole.getDocument().addDocumentListener(this);
				if (listener != null){
					listener.executionStarted();
				}
			}
		}
	}

	@Override
	public void consolesRemoved(IConsole[] consoles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		//we assume that only one documentChanged event is fired by eclipse
		//after the execution is finished
		//and it is contains the whole output
		if (listener != null){
			listener.executionFinished(event.getText());
		}
	}
}
