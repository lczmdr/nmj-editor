package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.nmt.nmj.editor.ICommandIds;

public class ListViewAction extends Action {

    private final IWorkbenchWindow window;

    public ListViewAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_LIST_VIEW);
        setActionDefinitionId(ICommandIds.CMD_LIST_VIEW);
    }

    public void run() {
        MessageDialog.openError(window.getShell(), "Error", "List view");
    }
}
