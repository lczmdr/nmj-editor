package com.nmt.nmj.editor.view;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.dialog.WallMovieInformationDialog;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Video;

public class WallView extends ViewPart {

    private static final String NO_POSTER_IMAGE = "/icons/no-poster.jpg";

    public static final String ID = "com.nmt.nmj.editor.wallView";

    private IWorkbenchWindow window;
    private Composite posterComposite;

    private ScrolledComposite scrolledComposite;

    public void createPartControl(Composite parent) {

        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        createPosterComposite();
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        Label l = new Label(posterComposite, SWT.NONE);
        l.setText("testing");
    }

    private void createPosterComposite() {
        posterComposite = new Composite(scrolledComposite, SWT.NONE);
        posterComposite.setBackground(new Color(window.getShell().getDisplay(), 0, 0, 0));
        scrolledComposite.setContent(posterComposite);
    }

    @Override
    public void setFocus() {

    }

    public void refresh() {
        try {
            if (Application.getDatabaseService().isConnected()) {
                posterComposite.dispose();
                createPosterComposite();
                List<Video> movies = Application.getDatabaseService().getAllMovies();
                int moviesByRow = posterComposite.getBounds().width / 130;
                int height = (int) (Math.ceil(movies.size() / moviesByRow) * 200);
                posterComposite.setLayout(new GridLayout(moviesByRow, false));
                scrolledComposite.setMinSize(posterComposite.computeSize(1000, height));
                for (final Video video : movies) {
                    final Canvas posterImageCanvas = new Canvas(posterComposite, SWT.NO_REDRAW_RESIZE);
                    posterImageCanvas.setToolTipText(video.getTitle());
                    posterImageCanvas.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseDown(MouseEvent e) {
                            try {
                                Application.getDatabaseService().getDetailedInformation(video);
                                WallMovieInformationDialog dialog = new WallMovieInformationDialog(window.getShell(), video);
                                dialog.open();
                            } catch (NmjEditorException e1) {
                                MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
                            }
                        }
                    });
                    posterImageCanvas.addMouseMoveListener(new MouseMoveListener() {
                        @Override
                        public void mouseMove(MouseEvent e) {
                            posterImageCanvas.setCursor(new Cursor(window.getShell().getDisplay(), SWT.CURSOR_HAND));
                        }
                    });
                    posterImageCanvas.setLayoutData(new GridData(124, 183));
                    posterImageCanvas.addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            Image posterImage = (Image) posterImageCanvas.getData("double-buffer-image");
                            if (posterImage == null) {
                                if (!video.getPosterImage().equals("")) {
                                    if (new File(video.getPosterImage()).exists()) {
                                        posterImage = new Image(window.getShell().getDisplay(), video.getPosterImage());
                                    } else {
                                        posterImage = new Image(window.getShell().getDisplay(), ListView.class
                                                .getResourceAsStream(NO_POSTER_IMAGE));
                                    }
                                } else {
                                    posterImage = new Image(window.getShell().getDisplay(), ListView.class
                                            .getResourceAsStream(NO_POSTER_IMAGE));
                                }
                                posterImageCanvas.setData("double-buffer-image", posterImage);
                            }
                            e.gc.drawImage(posterImage, 0, 0);
                        }
                    });
                }
                posterComposite.layout();
                posterComposite.pack(true);
            } else {
                posterComposite.dispose();
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }
}