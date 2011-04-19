package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.sqlite.SQLiteConnector;

public class OpenDatabaseAction extends Action {

    private final IWorkbenchWindow window;

    public OpenDatabaseAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_OPEN_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_OPEN_DATABASE);
        setImageDescriptor(ImageDescriptor.createFromFile(OpenDatabaseAction.class, "/icons/open.png"));
    }

    public void run() {
        if (window != null) {
            FileDialog fileDialog = new FileDialog(window.getShell(), SWT.OPEN);
            fileDialog.setText("Select the database...");
            fileDialog.setFilterExtensions(new String[] { "*.db", "*.*" });
            fileDialog.setFilterNames(new String[] { "Database Files (*.db)", "All Files (*.*)" });
            String selectedFile = fileDialog.open();
            if (selectedFile != null) {
                MessageDialog.openInformation(window.getShell(), "Selected file", selectedFile);
                SQLiteConnector connector = new SQLiteConnector();
                try {
                    connector.connect(selectedFile);
                } catch (ClassNotFoundException e) {
                    MessageDialog.openError(window.getShell(), "Error", "SQLlite drivers missing.");
                    return;
                }
                Application.setSqliteConnector(connector);
            }
        }
    }
}
