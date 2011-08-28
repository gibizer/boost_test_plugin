package boost_unit_test_plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;
import org.junit.Before;
import org.junit.Test;

public class Test_TestExecutionMonitor {
	
	private TestExecutionMonitor monitor;
	private IConsoleManager mgr;
	private TestExecutionListener listener;
	private TextConsole console;
	private IConsole[] consoles;
	private IDocument doc;
	
	
	@Before
	public void setUp() throws Exception {
		mgr = mock(IConsoleManager.class);
		listener = mock(TestExecutionListener.class);
		monitor = new TestExecutionMonitor(mgr);
		console = mock(TextConsole.class);
		consoles = new IConsole[1];
		consoles[0] = console;
		doc = mock(IDocument.class);
	}
	
	@Test
	public void AtStartMonitorSubscribesToNewConsolesInConsoleManager() {
		monitor.start();
		verify(mgr).addConsoleListener(monitor);
	}
	
	@Test
	public void MonitorSubscribesToSpecificExecutionConsole(){
		monitor.start();

		when(console.getType()).thenReturn(TestExecutionMonitor.CONSOLE_TYPE);
		when(console.getName()).thenReturn("boost_example.exe [C/C++ Application] D:\\Program\\eclipse_workspace_boost\\boost_example\\Debug\\boost_example.exe (2011.08.25. 23:19)");

		when(console.getDocument()).thenReturn(doc);
		
		
		monitor.consolesAdded(consoles);

		verify(mgr).addConsoleListener(monitor);
		verify(doc).addDocumentListener(monitor);		
	}
	
	@Test
	public void MonitorDoesNotSubscribeToOtherConsolesWrongConsoleType(){
		monitor.start();

		when(console.getType()).thenReturn("");
		when(console.getName()).thenReturn("boost_example.exe [C/C++ Application] D:\\Program\\eclipse_workspace_boost\\boost_example\\Debug\\boost_example.exe (2011.08.25. 23:19)");

				
		monitor.consolesAdded(consoles);

		verify(mgr).addConsoleListener(monitor);
		
	}

	@Test
	public void MonitorDoesNotSubscribeToOtherConsolesWrongConsoleName(){
		monitor.start();

		when(console.getType()).thenReturn(TestExecutionMonitor.CONSOLE_TYPE);
		when(console.getName()).thenReturn("");

				
		monitor.consolesAdded(consoles);

		verify(mgr).addConsoleListener(monitor);
	}
	
	@Test
	public void MonitorNotifiesExecutionListenerAboutExecution(){
		monitor.start();
		monitor.addExecutionListener(listener);

		when(console.getType()).thenReturn(TestExecutionMonitor.CONSOLE_TYPE);
		when(console.getName()).thenReturn("boost_example.exe [C/C++ Application] D:\\Program\\eclipse_workspace_boost\\boost_example\\Debug\\boost_example.exe (2011.08.25. 23:19)");

		when(console.getDocument()).thenReturn(doc);
		
		
		monitor.consolesAdded(consoles);

		verify(mgr).addConsoleListener(monitor);
		verify(doc).addDocumentListener(monitor);		
		verify(listener).executionStarted();
		
		DocumentEvent event = mock(DocumentEvent.class);
		final String CONSOLE_CONTENT = "Running 4 test cases...";
		when(event.getText()).thenReturn(CONSOLE_CONTENT);
		//when(console.getName()).thenReturn("<terminated> boost_example.exe [C/C++ Application] D:\\Program\\eclipse_workspace_boost\\boost_example\\Debug\\boost_example.exe (2011.08.25. 23:19)");
		monitor.documentChanged(event);
		
		verify(listener).executionFinished(CONSOLE_CONTENT);
		
	}
}
