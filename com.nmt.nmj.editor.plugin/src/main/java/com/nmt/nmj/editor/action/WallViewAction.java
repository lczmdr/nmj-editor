package com.nmt.nmj.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.nmt.nmj.editor.ICommandIds;
import com.nmt.nmj.editor.nls.InternationalizationMessages;
import com.nmt.nmj.editor.perspective.WallPerspective;
import com.nmt.nmj.editor.view.WallView;

public class WallViewAction extends Action {

    private final IWorkbenchWindow window;

    public WallViewAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setToolTipText(label);
        setId(com.nmt.nmj.editor.ICommandIds.CMD_WALL_VIEW);
        setActionDefinitionId(ICommandIds.CMD_WALL_VIEW);
    }

    public void run() {
        try {
            PlatformUI.getWorkbench().showPerspective(WallPerspective.ID, window);
            try {
                WallView wallView = (WallView) window.getActivePage().showView(WallView.ID);
                wallView.refresh();
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error,
                        "Wall view is missing");
            }
        } catch (WorkbenchException e) {
            MessageDialog.openError(window.getShell(), InternationalizationMessages.common_error,
                    "Error opening perspective:" + e.getMessage());
        }
    }
}
