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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Video;

public class WallView extends ViewPart {

    public static final String ID = "com.nmt.nmj.editor.wallView";

    private IWorkbenchWindow window;
    private Composite mainComposite;

    private ScrolledComposite scrolledComposite;

    private boolean updated;

    public void createPartControl(Composite parent) {

        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        mainComposite = new Composite(scrolledComposite, SWT.NONE);
//        mainComposite.setLayout(new GridLayout(8, false));
        // mainComposite.setSize(400, 400);
        mainComposite.setBackground(new Color(window.getShell().getDisplay(), 0, 0, 0));

        scrolledComposite.setContent(mainComposite);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        // scrolledComposite.setMinSize(mainComposite.computeSize(1000, 10000));

        // Composite imageCanvasComposite = new Composite(composite,
        // SWT.BORDER);
        // imageCanvasComposite.setLayout(new GridLayout(1, false));
        // GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        // gd.heightHint = 180;
        // gd.widthHint = 140;
        // imageCanvasComposite.setLayoutData(gd);

    }

    @Override
    public void setFocus() {

    }
    
    public void refresh() {
        try {
            if (Application.getDatabaseService().isConnected() && !updated) {
                List<Video> movies = Application.getDatabaseService().getAllMovies();
                int moviesByRow = mainComposite.getBounds().width / 130;
                int height = (int) (Math.ceil(movies.size() / moviesByRow) * 200);
                mainComposite.setLayout(new GridLayout(moviesByRow, false));
                scrolledComposite.setMinSize(mainComposite.computeSize(1000, height));
                for (final Video video : movies) {
                    final Canvas posterImageCanvas = new Canvas(mainComposite, SWT.NO_REDRAW_RESIZE);
                    posterImageCanvas.setToolTipText(video.getTitle());
                    posterImageCanvas.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseDown(MouseEvent e) {
                            System.out.println(video.getTitle() + " selected");
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
                            if (posterImage==null) {
                                if (!video.getPosterImage().equals("")) {
                                    if (new File(video.getPosterImage()).exists()) {
                                        posterImage = new Image(window.getShell().getDisplay(), video.getPosterImage());
                                        
                                    } else {
                                        posterImage = new Image(window.getShell().getDisplay(), ListView.class
                                                .getResourceAsStream("/icons/no-poster.jpg"));
                                    }
                                } else {
                                    posterImage = new Image(window.getShell().getDisplay(), ListView.class
                                            .getResourceAsStream("/icons/no-poster.jpg"));
                                }
                                posterImageCanvas.setData("double-buffer-image", posterImage);
                            }
                            e.gc.drawImage(posterImage, 0, 0);
                        }
                    });
                }
                mainComposite.layout();
                mainComposite.pack(true);
                updated = true;
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }
}