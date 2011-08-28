package boost_unit_test_plugin;

import org.eclipse.cdt.core.model.IBinaryContainer;

import org.eclipse.cdt.make.core.IMakeBuilderInfo;
import org.eclipse.cdt.make.core.MakeBuilder;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.IBuildConsoleEvent;
import org.eclipse.cdt.ui.IBuildConsoleListener;
import org.eclipse.cdt.ui.IBuildConsoleManager;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.internal.core.InputStreamMonitor;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.debug.internal.ui.views.console.ProcessConsoleManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private final class DocumentListener implements IDocumentListener {
		@Override
		public void documentChanged(DocumentEvent event) {
			System.out.println("console's content changed: "
					+ event.getText());
			System.out.println(console.getName());
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "boost_unit_test_plugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static IConsole console;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

//		final IBuildConsoleManager mgr = CUIPlugin.getDefault()
//				.getConsoleManager();
//		
//		final DocumentListener documentListener = new DocumentListener();
//		
//		mgr.addConsoleListener(new IBuildConsoleListener() {
//
//			@Override
//			public void consoleChange(IBuildConsoleEvent event) {
//				IDocument doc = mgr.getConsoleDocument(event.getProject());
//				if (event.getType() == IBuildConsoleEvent.CONSOLE_START) {
//					System.out.println("CONSOLE_START");
//					printConsoles();
//					doc.addDocumentListener(documentListener);
//				}else{
//					System.out.println("CONSOLE_END");
//					doc.removeDocumentListener(documentListener);
//				}
//
//			}
//		});
		
		
		final DocumentListener listener = new DocumentListener();
		ConsolePlugin.getDefault().getConsoleManager().addConsoleListener(new IConsoleListener() {
		
			@Override
			public void consolesRemoved(IConsole[] consoles) {
				for (IConsole console : consoles){
					System.out.println("Console removed: " + console.getName());
					
				}				
			}
			
			@Override
			public void consolesAdded(IConsole[] consoles) {
				for (IConsole console : consoles){
					System.out.println("Console added: name=" + console.getName() + "type=" + console.getType());
					if ("org.eclipse.debug.ui.ProcessConsoleType".equals(console.getType()) && console.getName().contains("[C/C++ Application]")){
						System.out.println("found!");
						Activator.console = console;
						TextConsole pc = (TextConsole)console;
						IDocument doc = pc.getDocument();
						doc.addDocumentListener(listener);
						
					}
				}
			}
		});

	}

	private void printConsoles() {
		//<terminated> boost_example.exe [C/C++ Application] D:\Program\eclipse_workspace_boost\boost_example\Debug\boost_example.exe (2011.08.25. 23:19)		
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		for (int i = 0; i < consoles.length; i++){
			System.out.println(consoles[i].getName());
		}
		System.out.println();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
