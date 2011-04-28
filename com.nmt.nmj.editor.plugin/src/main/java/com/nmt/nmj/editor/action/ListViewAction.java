package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.i8n.InternationalizationMessages;
import com.nmt.nmj.editor.perspective.ListPerspective;

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
        try {
            PlatformUI.getWorkbench().showPerspective(ListPerspective.ID, window);
        } catch (WorkbenchException e) {
            MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error,
                    "Error opening perspective:" + e.getMessage());
        }
    }
}
