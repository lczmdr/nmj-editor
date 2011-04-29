package com.nmt.nmj.editor.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.nmt.nmj.editor.model.Video;
import com.nmt.nmj.editor.view.components.MovieInformationComposite;

public class MovieInformationDialog extends Dialog {

    private MovieInformationComposite movieInformationComposite;
    private Video video;

    public MovieInformationDialog(Shell parent, Video video) {
        super(parent);
        this.video = video;
    }

    protected Control createDialogArea(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        parent.setLayout(layout);
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        movieInformationComposite = new MovieInformationComposite(parent, SWT.NONE);
        movieInformationComposite.setLayout(layout);
        movieInformationComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        movieInformationComposite.initialize();
        movieInformationComposite.showMovieInformation(video);
        movieInformationComposite.setVisible(true);
        return movieInformationComposite;
    }

    protected void createButtonsForButtonBar(Composite parent) {

    }

}
