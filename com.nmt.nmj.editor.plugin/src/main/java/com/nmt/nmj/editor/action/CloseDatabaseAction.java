package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.nls.InternationalizationMessages;
import com.nmt.nmj.editor.perspective.ListPerspective;
import com.nmt.nmj.editor.perspective.WallPerspective;
import com.nmt.nmj.editor.view.ListView;
import com.nmt.nmj.editor.view.WallView;

public class CloseDatabaseAction extends Action {

    private final IWorkbenchWindow window;

    public CloseDatabaseAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_CLOSE_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_CLOSE_DATABASE);
    }

    public void run() {
        try {
            Application.getDatabaseService().closeConnection();
            try {
                PlatformUI.getWorkbench().showPerspective(WallPerspective.ID, window);
                WallView wallView = (WallView) window.getActivePage().showView(WallView.ID);
                wallView.refresh();
                PlatformUI.getWorkbench().showPerspective(ListPerspective.ID, window);
                ListView listView = (ListView) window.getActivePage().showView(ListView.ID);
                listView.refresh();
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error,
                        "List view is missing");
            } catch (WorkbenchException e) {
                MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error,
                        "List perspective is missing");
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error, e.getMessage());
        }
    }
}
