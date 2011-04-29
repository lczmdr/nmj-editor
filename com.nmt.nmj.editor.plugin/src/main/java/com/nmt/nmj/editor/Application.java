package com.nmt.nmj.editor;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.nmt.nmj.editor.backend.JukeboxDatabaseService;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.nls.NlsMessages;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    private static JukeboxDatabaseService databaseService;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
     * IApplicationContext)
     */
    public Object start(IApplicationContext context) throws Exception {
        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART)
                return IApplication.EXIT_RESTART;
            else
                return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    public void stop() {
        if (databaseService != null) {
            try {
                databaseService.closeConnection();
            } catch (NmjEditorException e) {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        NlsMessages.common_error, e.getMessage());
            }
        }
        if (!PlatformUI.isWorkbenchRunning())
            return;
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }

    public static JukeboxDatabaseService getDatabaseService() {
        return databaseService;
    }

    public static void setDatabaseService(JukeboxDatabaseService databaseService) {
        Application.databaseService = databaseService;
    }

}
