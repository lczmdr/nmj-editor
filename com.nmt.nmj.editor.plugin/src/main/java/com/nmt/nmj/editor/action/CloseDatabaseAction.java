package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.perspective.ListPerspective;
import com.nmt.nmj.editor.view.ListView;

public class CloseDatabaseAction extends Action {

    private final IWorkbenchWindow window;

    public CloseDatabaseAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_CLOSE_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_CLOSE_DATABASE);
        setImageDescriptor(ImageDescriptor.createFromFile(CloseDatabaseAction.class, "/icons/close.png"));
    }

    public void run() {
        try {
            Application.getDatabaseService().closeConnection();
            try {
                PlatformUI.getWorkbench().showPerspective(ListPerspective.ID, window);
                ListView editorView = (ListView) window.getActivePage().showView(ListView.ID);
                editorView.refresh();
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), "Error", "List view is missing");
            } catch (WorkbenchException e) {
                MessageDialog.openError(window.getShell(), "Error", "List perspective is missing");
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }
}
