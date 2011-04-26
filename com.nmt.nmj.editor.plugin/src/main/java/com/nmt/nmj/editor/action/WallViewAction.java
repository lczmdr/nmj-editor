package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.nmt.nmj.editor.ICommandIds;

public class WallViewAction extends Action {

    private final IWorkbenchWindow window;

    public WallViewAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_CLOSE_DATABASE);
        setActionDefinitionId(ICommandIds.CMD_CLOSE_DATABASE);
    }

    public void run() {
        MessageDialog.openError(window.getShell(), "Error", "Wall view");
    }
}
