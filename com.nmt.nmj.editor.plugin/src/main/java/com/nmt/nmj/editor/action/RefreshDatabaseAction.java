package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.view.ListView;

public class RefreshDatabaseAction extends Action {

    private final IWorkbenchWindow window;

    public RefreshDatabaseAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_REFRESH_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_REFRESH_DATABASE);
        setImageDescriptor(ImageDescriptor.createFromFile(RefreshDatabaseAction.class, "/icons/refresh.png"));
    }

    public void run() {
        if (window != null) {
            String fileName = Application.getDatabaseService().getFileName();
            if (fileName == null || fileName.length() == 0) {
                MessageDialog.openInformation(window.getShell(), "Information",
                        "No current database connection. First open one!");
                return;
            }
            try {
                Application.getDatabaseService().openConnection(fileName);
            } catch (NmjEditorException e1) {
                MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
                return;
            }
            try {
                ListView editorView = (ListView) window.getActivePage().showView(ListView.ID);
                editorView.refresh();
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), "Error", "Editor view is missing");
                e.printStackTrace();
            }
        }
    }
}
