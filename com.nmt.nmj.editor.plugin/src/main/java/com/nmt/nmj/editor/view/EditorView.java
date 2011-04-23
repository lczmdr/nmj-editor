package com.nmt.nmj.editor.view;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.dialog.CalendarDialog;
import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Video;
import com.nmt.nmj.editor.view.provider.MovieContentProvider;
import com.nmt.nmj.editor.view.provider.MovieLabelProvider;

public class EditorView extends ViewPart {

    public static final String ID = "com.nmt.nmj.editor.editorView";

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
    private Composite detailedInformationComposite;
    private TableViewer movieTableViewer;
    private Label informationLabel;
    private MovieSorter movieTableSorter;
    private Label movieTitle;
    private Video currentVideo;
    private Text releaseDateText;
    private org.eclipse.swt.widgets.List keywordsList;
    private org.eclipse.swt.widgets.List directorsList;
    private org.eclipse.swt.widgets.List genresList;
    private org.eclipse.swt.widgets.List castingList;
    private Label fileNameLabel;
    private Text synopsisText;

    public void createPartControl(Composite parent) {

        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING,
                true, false));
        layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.numColumns = 1;
        mainComposite.setLayout(layout);

        informationLabel = new Label(mainComposite, SWT.WRAP);
        informationLabel.setText("Current Database: ");

        TabFolder tabFolder = new TabFolder(mainComposite, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 260;
        tabFolder.setLayoutData(gd);

        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Movies");

        tabItem.setControl(createMovieTable(tabFolder));

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("TV Shows");

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Music");

        createDetailedInformationGroup();

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
                IStructuredSelection selection = (IStructuredSelection) movieTableViewer.getSelection();
                currentVideo = (Video) selection.getFirstElement();
                if (currentVideo != null) {
                    try {
                        obtainExtraInformation(currentVideo);
                        movieTitle.setText("Movie: " + currentVideo.getTitle());
                        movieTitle.pack();
                        releaseDateText.setText(currentVideo.getReleaseDate());
                        fileNameLabel.setText(currentVideo.getFileName());
                        fileNameLabel.pack();
                        synopsisText.setText(currentVideo.getSynopsis());
                        fillInformationList(currentVideo.getGenres(), genresList);
                        fillInformationList(currentVideo.getDirectors(), directorsList);
                        fillInformationList(currentVideo.getCasting(), castingList);
                        fillInformationList(currentVideo.getKeywords(), keywordsList);
                        detailedInformationComposite.setVisible(true);
                        detailedInformationComposite.pack();
                    } catch (NmjEditorException e1) {
                        MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
                    }
                }
            }

            private void fillInformationList(List<String> information, org.eclipse.swt.widgets.List informationList) {
                informationList.removeAll();
                for (String info : information) {
                    informationList.add(info);
                }
            }
        });

        return composite;
    }

    private void createDetailedInformationGroup() {
        Group detailedInformationGroup = new Group(mainComposite, SWT.SHADOW_ETCHED_IN);
        detailedInformationGroup.setText("Detailed information");
        detailedInformationGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        detailedInformationGroup.setLayout(layout);

        detailedInformationComposite = new Composite(detailedInformationGroup, SWT.NONE);
        detailedInformationComposite.setLayout(layout);
        detailedInformationComposite.setVisible(false);

        movieTitle = new Label(detailedInformationComposite, SWT.WRAP);
        Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
        if (boldFont.getFontData().length == 1) {
            // TODO: font size not working
            FontData fontData = boldFont.getFontData()[0];
            fontData.setHeight(16);
            boldFont = new Font(window.getShell().getDisplay(), boldFont.getFontData()[0]);
        }
        movieTitle.setFont(boldFont);

        fileNameLabel = new Label(detailedInformationComposite, SWT.NONE);
        fileNameLabel.setText("Filename:");
        fileNameLabel.pack();

        Composite doubleColumnComposite = new Composite(detailedInformationComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        doubleColumnComposite.setLayout(layout);

        createReleaseDateWidget(doubleColumnComposite);

        Composite composite = new Composite(doubleColumnComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        genresList = createListControl(composite, "Genres");
        directorsList = createListControl(composite, "Directors");
        castingList = createListControl(composite, "Casting");
        keywordsList = createListControl(composite, "Keywords");

        // white space
        new Label(doubleColumnComposite, SWT.NONE);

        Composite sinopsisComposite = new Composite(doubleColumnComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        sinopsisComposite.setLayout(layout);

        Label label = new Label(sinopsisComposite, SWT.NONE);
        label.setText("Synopsis");
        synopsisText = new Text(sinopsisComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gd = new GridData();
        gd.widthHint = 300;
        gd.heightHint = 70;
        synopsisText.setLayoutData(gd);
    }

    private void createReleaseDateWidget(Composite parent) {
        Composite releaseDateContainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        releaseDateContainer.setLayout(layout);

        Label l = new Label(releaseDateContainer, SWT.NONE);
        l.setText("Release Date:");
        l.pack();

        releaseDateText = new Text(releaseDateContainer, SWT.BORDER);
        releaseDateText.setEditable(false);
        GridData gd = new GridData();
        gd.widthHint = 80;
        releaseDateText.setLayoutData(gd);

        Button openCalendar = new Button(releaseDateContainer, SWT.PUSH);
        openCalendar.setText("...");
        openCalendar.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (currentVideo != null) {
                    Date releaseDate = CalendarDialog.convertDate(currentVideo.getReleaseDate());
                    CalendarDialog calendarDialog = new CalendarDialog(window.getShell(), releaseDate);
                    if (calendarDialog.open() == Window.OK) {
                        releaseDateText.setText(calendarDialog.getSelectedDate());
                        currentVideo.setReleaseDate(calendarDialog.getSelectedDate());
                    }
                }
            }
        });
    }

    private org.eclipse.swt.widgets.List createListControl(Composite parent, String label) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        Label l = new Label(composite, SWT.NONE);
        l.setText(label);
        l.pack();

        org.eclipse.swt.widgets.List keywordsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER
                | SWT.V_SCROLL);
        GridData gd = new GridData(SWT.BORDER);
        gd.heightHint = 70;
        gd.widthHint = 120;
        keywordsList.setLayoutData(gd);
        return keywordsList;
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
            if (Application.getSqliteService().isConnected()) {
                informationLabel.setText("Current Database: " + Application.getSqliteService().getFileName());
                informationLabel.pack();
                List<Video> videos = Application.getSqliteService().getAllMovies();
                this.movieTableViewer.setInput(videos);
                mainComposite.layout();
            } else {
                informationLabel.setText("Current Database: ");
                movieTableViewer.setInput(null);
                detailedInformationComposite.setVisible(false);
            }
        } catch (NmjEditorException e) {
            MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        }
    }

    private void obtainExtraInformation(Video video) throws NmjEditorException {
        Application.getSqliteService().getDetailedInformation(video);
    }

}