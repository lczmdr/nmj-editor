package com.nmt.nmj.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.nmt.nmj.editor.action.CloseDatabaseAction;
import com.nmt.nmj.editor.action.OpenDatabaseAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;
    private Action openDatabaseAction;
    private Action closeDatabaseAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setImageDescriptor(ImageDescriptor.createFromFile(ApplicationActionBarAdvisor.class,
                "/icons/exit.png"));
        exitAction.setToolTipText("Exit");
        register(exitAction);

        openDatabaseAction = new OpenDatabaseAction(window, "Open Database");
        register(openDatabaseAction);

        closeDatabaseAction = new CloseDatabaseAction(window, "Close Database");
        register(closeDatabaseAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(openDatabaseAction);
        toolbar.add(closeDatabaseAction);
        toolbar.add(new Separator());
        toolbar.add(exitAction);
    }

}
