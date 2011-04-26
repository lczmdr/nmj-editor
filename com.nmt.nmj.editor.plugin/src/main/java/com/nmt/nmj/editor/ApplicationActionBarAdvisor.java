package com.nmt.nmj.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
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
import com.nmt.nmj.editor.action.ListViewAction;
import com.nmt.nmj.editor.action.OpenDatabaseAction;
import com.nmt.nmj.editor.action.RefreshDatabaseAction;
import com.nmt.nmj.editor.action.WallViewAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;
    private Action openDatabaseAction;
    private Action refreshDatabaseAction;
    private Action closeDatabaseAction;
    private Action wallViewAction;
    private Action listViewAction;

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

        refreshDatabaseAction = new RefreshDatabaseAction(window, "Refresh");
        register(refreshDatabaseAction);

        wallViewAction = new WallViewAction(window, "Wall");
        register(wallViewAction);

        listViewAction = new ListViewAction(window, "List");
        register(listViewAction);

        closeDatabaseAction = new CloseDatabaseAction(window, "Close Database");
        register(closeDatabaseAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager databaseMenu = new MenuManager("&Database");
        MenuManager viewMenu = new MenuManager("&View as...");

        menuBar.add(databaseMenu);
        menuBar.add(viewMenu);

        databaseMenu.add(openDatabaseAction);
        databaseMenu.add(refreshDatabaseAction);
        databaseMenu.add(closeDatabaseAction);
        databaseMenu.add(new Separator());
        databaseMenu.add(exitAction);

        viewMenu.add(listViewAction);
        viewMenu.add(wallViewAction);
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(openDatabaseAction);
        toolbar.add(refreshDatabaseAction);
        toolbar.add(closeDatabaseAction);
        toolbar.add(new Separator());
        toolbar.add(exitAction);
    }

}
