package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.view.EditorView;

public class OpenDatabaseAction extends Action {

    private final IWorkbenchWindow window;

    public OpenDatabaseAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_OPEN_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_OPEN_DATABASE);
        setImageDescriptor(ImageDescriptor.createFromFile(OpenDatabaseAction.class, "/icons/open.png"));
    }

    public void run() {
        if (window != null) {
            FileDialog fileDialog = new FileDialog(window.getShell(), SWT.OPEN);
            fileDialog.setFilterPath(System.getProperty("user.home"));
            fileDialog.setText("Select the database...");
            fileDialog.setFilterExtensions(new String[] { "*.db", "*.*" });
            fileDialog.setFilterNames(new String[] { "Database Files (*.db)", "All Files (*.*)" });
            String selectedFile = fileDialog.open();
            if (selectedFile != null) {
                try {
                    Application.getDatabaseService().openConnection(selectedFile);
                } catch (NmjEditorException e1) {
                    MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
                    return;
                }
                try {
                    EditorView editorView = (EditorView) window.getActivePage().showView(EditorView.ID);
                    editorView.refresh();
                } catch (PartInitException e) {
                    MessageDialog.openError(window.getShell(), "Error", "Editor view is missing");
                    e.printStackTrace();
                }
            }
        }
    }
}
