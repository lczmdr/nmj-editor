package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.sqlite.SQLiteConnector;
import com.nmt.nmj.editor.view.EditorView;

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
            SQLiteConnector sqliteConnector = Application.getSqliteConnector();
            if (sqliteConnector != null) {
                sqliteConnector.disconnect();
                try {
                    EditorView editorView = (EditorView) window.getActivePage().showView(EditorView.ID);
                    editorView.refresh();
                } catch (PartInitException e) {
                    MessageDialog.openError(window.getShell(), "Error", "Editor view is missing");
                    e.printStackTrace();
                }
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }
}
