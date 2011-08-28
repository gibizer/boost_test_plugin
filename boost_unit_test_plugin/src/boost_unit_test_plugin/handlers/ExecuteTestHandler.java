package boost_unit_test_plugin.handlers;

import org.eclipse.cdt.core.model.IBinaryContainer;
import org.eclipse.cdt.core.parser.util.DebugUtil;
import org.eclipse.cdt.launch.LaunchUtils;
import org.eclipse.cdt.launch.internal.ui.LaunchUIPlugin;
import org.eclipse.cdt.make.core.IMakeBuilderInfo;
import org.eclipse.cdt.make.core.MakeBuilder;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ExecuteTestHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ExecuteTestHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Boost_unit_test_plugin",
				"boooo");
		
		ILaunchConfiguration[] configs;
		try {
			configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
			for (int i = 0; i < configs.length; i++){
				System.out.println(configs[i].getMemento());
			}
			System.out.println();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//DebugUITools.buildAndLaunch(configuration, ILaunchManager.RUN_MODE, null);
		//System.out.println(event.getApplicationContext().getClass().getName() + ", " + event.getApplicationContext());
		
//		final String S_C_PROJECTS_VIEW = "org.eclipse.cdt.ui.CView";
//
//		IResource resource = null;
//		IStructuredSelection ssel = null;
//		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow();
//		if (activeWindow != null) {
//			ISelection sel = activeWindow.getSelectionService().getSelection(
//					S_C_PROJECTS_VIEW);
//			if (sel instanceof IStructuredSelection) {
//				ssel = (IStructuredSelection) sel;
//			}
//			Object obj = ssel.getFirstElement();
//			if (obj instanceof IResource)
//				resource = (IResource) obj;
//			else if (obj instanceof IBinaryContainer) {
//				IBinaryContainer bincon = (IBinaryContainer) obj;
//				resource = bincon.getUnderlyingResource();
//			} else if (obj instanceof IAdaptable) {
//				IAdaptable adaptable = (IAdaptable) obj;
//				resource = (IResource) adaptable.getAdapter(IResource.class);
//			}
//		}
//
//
//
//		IMakeBuilderInfo builderInfo;
//		try {
//			builderInfo = MakeCorePlugin.createBuildInfo(resource.getProject(),
//					MakeBuilder.BUILDER_ID);
//			System.out.println("!!!!!!!" + builderInfo.getFullBuildTarget());		
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}
}
