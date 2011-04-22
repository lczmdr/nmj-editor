package com.nmt.nmj.editor;

import java.sql.SQLException;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.nmt.nmj.editor.sqlite.SQLiteConnector;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    private static SQLiteConnector sqliteConnector;

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
        if (sqliteConnector != null) {
            try {
                sqliteConnector.getConnection().close();
            } catch (SQLException e) {
                // TODO: ???
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

    public static void setSqliteConnector(SQLiteConnector sqliteConnector) {
        Application.sqliteConnector = sqliteConnector;
    }

    public static SQLiteConnector getSqliteConnector() {
        return sqliteConnector;
    }
}
