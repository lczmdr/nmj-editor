package com.nmt.nmj.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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
import com.nmt.nmj.editor.nls.NlsMessages;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;
    private Action openDatabaseAction;
    private Action refreshDatabaseAction;
    private Action closeDatabaseAction;
    private Action listViewAction;
    private Action wallViewAction;
    private IWorkbenchAction aboutAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setToolTipText(NlsMessages.menu_exit_tooltip);
        register(exitAction);

        openDatabaseAction = new OpenDatabaseAction(window, NlsMessages.menu_open_database);
        register(openDatabaseAction);

        refreshDatabaseAction = new RefreshDatabaseAction(window, NlsMessages.menu_refresh);
        register(refreshDatabaseAction);

        closeDatabaseAction = new CloseDatabaseAction(window, NlsMessages.menu_close_database);
        register(closeDatabaseAction);

        listViewAction = new ListViewAction(window, NlsMessages.menu_list_view);
        register(listViewAction);

        wallViewAction = new WallViewAction(window, NlsMessages.menu_wall_view);
        register(wallViewAction);

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager databaseMenu = new MenuManager(NlsMessages.menu_database);
        MenuManager layoutMenu = new MenuManager(NlsMessages.menu_switch_view);
        MenuManager helpMenu = new MenuManager(NlsMessages.menu_switch_view);

        menuBar.add(databaseMenu);
        menuBar.add(layoutMenu);
        menuBar.add(helpMenu);

        databaseMenu.add(openDatabaseAction);
        databaseMenu.add(refreshDatabaseAction);
        databaseMenu.add(closeDatabaseAction);
        databaseMenu.add(new Separator());
        databaseMenu.add(exitAction);

        layoutMenu.add(listViewAction);
        layoutMenu.add(wallViewAction);

        helpMenu.add(aboutAction);
    }

    // protected void fillCoolBar(ICoolBarManager coolBar) {
    // IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
    // coolBar.add(new ToolBarContributionItem(toolbar, "main"));
    // toolbar.add(openDatabaseAction);
    // toolbar.add(refreshDatabaseAction);
    // toolbar.add(closeDatabaseAction);
    // toolbar.add(new Separator());
    // toolbar.add(exitAction);
    // }

}
