package com.nmt.nmj.editor.view;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Video;
import com.nmt.nmj.editor.view.components.MovieInformationComposite;
import com.nmt.nmj.editor.view.provider.MovieContentProvider;
import com.nmt.nmj.editor.view.provider.MovieLabelProvider;
import com.nmt.nmj.editor.view.sorter.MovieSorter;

public class ListView extends ViewPart {

    public static final String ID = "com.nmt.nmj.editor.listView";

    private final String VIDEO_ID = "ID";
    private final String VIDEO_TITLE = "Name";
    private final String VIDEO_RELEASE_DATE = "Release Date";
    private final String VIDEO_RUNTIME = "Runtime";
    private final String VIDEO_RATING = "Rating";
    private final String VIDEO_SYSTEM = "System";
    private final String VIDEO_CODEC = "Video Codec";
    private final String VIDEO_DIMENSIONS = "Dimensions";
    private final String VIDEO_FPS = "FPS";

    private String[] columnNames = new String[] { VIDEO_ID, VIDEO_TITLE, VIDEO_RELEASE_DATE, VIDEO_RUNTIME,
            VIDEO_RATING, VIDEO_SYSTEM, VIDEO_CODEC, VIDEO_DIMENSIONS, VIDEO_FPS };

    private IWorkbenchWindow window;
    private Composite mainComposite;
    private MovieInformationComposite movieInformationComposite;
    private TableViewer movieTableViewer;
    private Label informationLabel;
    private MovieSorter movieTableSorter;
    private Video currentVideo;

    public void createPartControl(Composite parent) {

        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING,
                true, false));
        GridLayout layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.numColumns = 1;
        mainComposite.setLayout(layout);

        informationLabel = new Label(mainComposite, SWT.WRAP);
        informationLabel.setText("Current Database: ");

        TabFolder tabFolder = new TabFolder(mainComposite, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 220;
        tabFolder.setLayoutData(gd);

        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Movies");

        tabItem.setControl(createMovieTable(tabFolder));

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("TV Shows");

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Music");

        movieInformationComposite = new MovieInformationComposite(mainComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 1;
        movieInformationComposite.setLayout(layout);
        movieInformationComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        movieInformationComposite.initialize();
        movieInformationComposite.setVisible(false);
    }

    private Control createMovieTable(TabFolder tabFolder) {
        Composite composite = new Composite(tabFolder, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        Table movieTable = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.SCROLL_LINE | SWT.SINGLE
                | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        movieTable.setHeaderVisible(true);
        movieTable.setLinesVisible(true);

        TableColumn idTableColumn = new TableColumn(movieTable, SWT.CENTER);
        idTableColumn.setText(VIDEO_ID);
        idTableColumn.setWidth(70);
        idTableColumn.setResizable(false);
        idTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn titleTableColumn = new TableColumn(movieTable, SWT.CENTER);
        titleTableColumn.setText(VIDEO_TITLE);
        titleTableColumn.setWidth(300);
        titleTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RELEASE_DATE);
        tableColumn.setWidth(120);
        tableColumn.setResizable(false);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RUNTIME);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RATING);
        tableColumn.setWidth(80);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_SYSTEM);
        tableColumn.setWidth(120);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_CODEC);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_DIMENSIONS);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(movieTable, SWT.CENTER);
        tableColumn.setText(VIDEO_FPS);
        tableColumn.setWidth(100);

        movieTableViewer = new TableViewer(movieTable);
        movieTableViewer.setUseHashlookup(true);
        movieTableViewer.setColumnProperties(columnNames);

        movieTableViewer.setContentProvider(new MovieContentProvider());
        movieTableViewer.setLabelProvider(new MovieLabelProvider());

        movieTableSorter = new MovieSorter(titleTableColumn);
        movieTableViewer.setSorter(movieTableSorter);

        movieTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshSelectedMovieInformation();
            }
        });

        return composite;
    }

    private void refreshSelectedMovieInformation() {
        IStructuredSelection selection = (IStructuredSelection) movieTableViewer.getSelection();
        currentVideo = (Video) selection.getFirstElement();
        if (currentVideo != null) {
            try {
                obtainExtraInformation(currentVideo);
                movieInformationComposite.setMovie(currentVideo);
                movieInformationComposite.setVisible(true);
                movieInformationComposite.pack();
            } catch (NmjEditorException e1) {
                MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
            }
        }
    }

    private Listener selChangeListenerColumn = new Listener() {
        public void handleEvent(Event event) {
            movieTableSorter.setCurrentColumn((TableColumn) event.widget);
            movieTableViewer.refresh();
        }
    };

    public void setFocus() {
    }

    public void refresh() {
        try {
            if (Application.getDatabaseService().isConnected()) {
                informationLabel.setText("Current Database: " + Application.getDatabaseService().getFileName());
                informationLabel.pack();
                List<Video> videos = Application.getDatabaseService().getAllMovies();
                this.movieTableViewer.setInput(videos);
                mainComposite.layout();
                getViewSite().getActionBars().getStatusLineManager().setMessage(videos.size() + " movies");
                if (movieTableViewer.getTable().getItemCount() > 0) {
                    movieTableViewer.getTable().select(0);
                    refreshSelectedMovieInformation();
                }
            } else {
                informationLabel.setText("Current Database: ");
                movieTableViewer.setInput(null);
                movieInformationComposite.setVisible(false);
                getViewSite().getActionBars().getStatusLineManager().setMessage("");
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }

    private void obtainExtraInformation(Video video) throws NmjEditorException {
        Application.getDatabaseService().getDetailedInformation(video);
    }

}