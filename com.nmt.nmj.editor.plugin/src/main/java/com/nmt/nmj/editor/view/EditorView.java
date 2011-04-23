package com.nmt.nmj.editor.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.nmt.nmj.editor.sqlite.SQLQueries;
import com.nmt.nmj.editor.view.provider.VideoContentProvider;
import com.nmt.nmj.editor.view.provider.VideoLabelProvider;

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
    private Composite container;
    private TableViewer videoTableViewer;
    private Label informationLabel;
    private MovieSorter videoTableSorter;
    private Label movieTitle;
    private Video currentVideo;
    private Text releaseDateText;

    private Composite detailedInformationComposite;

    private org.eclipse.swt.widgets.List keywordsList;
    private org.eclipse.swt.widgets.List directorsList;
    private org.eclipse.swt.widgets.List genresList;
    private org.eclipse.swt.widgets.List castingList;

    private Label fileNameLabel;

    public void createPartControl(Composite parent) {

        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true,
                false));
        layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.numColumns = 1;
        container.setLayout(layout);

        informationLabel = new Label(container, SWT.WRAP);
        informationLabel.setText("Current Database: ");

        TabFolder tabFolder = new TabFolder(container, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 290;
        tabFolder.setLayoutData(gd);

        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Movies");

        tabItem.setControl(createVideoTable(tabFolder));

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("TV Shows");

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText("Music");

        createDetailedInformationGroup();

    }

    private Control createVideoTable(TabFolder tabFolder) {
        Composite composite = new Composite(tabFolder, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        Table videoTable = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.SCROLL_LINE | SWT.SINGLE
                | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        videoTable.setHeaderVisible(true);
        videoTable.setLinesVisible(true);

        TableColumn idTableColumn = new TableColumn(videoTable, SWT.CENTER);
        idTableColumn.setText(VIDEO_ID);
        idTableColumn.setWidth(70);
        idTableColumn.setResizable(false);
        idTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn titleTableColumn = new TableColumn(videoTable, SWT.CENTER);
        titleTableColumn.setText(VIDEO_TITLE);
        titleTableColumn.setWidth(300);
        titleTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RELEASE_DATE);
        tableColumn.setWidth(120);
        tableColumn.setResizable(false);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RUNTIME);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RATING);
        tableColumn.setWidth(80);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_SYSTEM);
        tableColumn.setWidth(120);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_CODEC);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_DIMENSIONS);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_FPS);
        tableColumn.setWidth(100);

        videoTableViewer = new TableViewer(videoTable);
        videoTableViewer.setUseHashlookup(true);
        videoTableViewer.setColumnProperties(columnNames);

        videoTableViewer.setContentProvider(new VideoContentProvider());
        videoTableViewer.setLabelProvider(new VideoLabelProvider());

        videoTableSorter = new MovieSorter(titleTableColumn);
        videoTableViewer.setSorter(videoTableSorter);

        videoTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) videoTableViewer.getSelection();
                currentVideo = (Video) selection.getFirstElement();
                if (currentVideo != null) {
                    try {
                        obtainExtraInformation(currentVideo);
                        movieTitle.setText("Movie: " + currentVideo.getTitle());
                        movieTitle.pack();
                        releaseDateText.setText(currentVideo.getReleaseDate());
                        fileNameLabel.setText(currentVideo.getFileName());
                        fileNameLabel.pack();
                        detailedInformationComposite.setVisible(true);
                        detailedInformationComposite.pack();
                        fillInformationList(currentVideo.getGenres(), genresList);
                        fillInformationList(currentVideo.getDirectors(), directorsList);
                        fillInformationList(currentVideo.getCasting(), castingList);
                        fillInformationList(currentVideo.getKeywords(), keywordsList);
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
        Group detailedInformationGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
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

        // white space
        // new Label(detailedInformationComposite, SWT.NONE);

        Composite doubleColumnComposite = new Composite(detailedInformationComposite, SWT.NONE | SWT.VERTICAL);
        layout = new GridLayout();
        layout.numColumns = 2;
        doubleColumnComposite.setLayout(layout);

        createReleaseDateWidget(doubleColumnComposite);

        Composite composite = new Composite(doubleColumnComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setText("Keywords");
        label.pack();
        keywordsList = createListControl(composite);
        label = new Label(composite, SWT.NONE);
        label.setText("Directors");
        label.pack();
        directorsList = createListControl(composite);
        label = new Label(composite, SWT.NONE);
        label.setText("Genres");
        label.pack();
        genresList = createListControl(composite);
        label = new Label(composite, SWT.NONE);
        label.setText("Casting");
        label.pack();
        castingList = createListControl(composite);
    }

    private void createReleaseDateWidget(Composite parent) {
        Composite releaseDateContainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        releaseDateContainer.setLayout(layout);

        Label l = new Label(releaseDateContainer, SWT.NONE | SWT.TOP);
        l.setText("Release Date:");
        l.pack();

        releaseDateText = new Text(releaseDateContainer, SWT.BORDER | SWT.TOP);
        releaseDateText.setEditable(false);
        GridData gd = new GridData();
        gd.widthHint = 80;
        releaseDateText.setLayoutData(gd);

        Button openCalendar = new Button(releaseDateContainer, SWT.PUSH | SWT.TOP);
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

    private org.eclipse.swt.widgets.List createListControl(Composite parent) {
        org.eclipse.swt.widgets.List keywordsList = new org.eclipse.swt.widgets.List(parent, SWT.BORDER | SWT.V_SCROLL);
        GridData gd = new GridData(SWT.BORDER);
        gd.heightHint = 70;
        gd.widthHint = 120;
        keywordsList.setLayoutData(gd);
        return keywordsList;
    }

    private Listener selChangeListenerColumn = new Listener() {
        public void handleEvent(Event event) {
            videoTableSorter.setCurrentColumn((TableColumn) event.widget);
            videoTableViewer.refresh();
        }
    };

    public void setFocus() {
    }

    public void refresh() {
        Connection connection = Application.getSqliteConnector().getConnection();
        try {
            if (!connection.isClosed()) {
                String fileName = Application.getSqliteConnector().getFileName();
                informationLabel.setText("Current Database: " + fileName);
                informationLabel.pack();
                List<Video> videos = createVideoStructure(connection);
                this.videoTableViewer.setInput(videos);
                container.layout();
            } else {
                informationLabel.setText("Current Database: ");
                videoTableViewer.setInput(null);
                detailedInformationComposite.setVisible(false);
            }
        } catch (SQLException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    "Error reading database", "An error happened trying to read the database: \n" + e.getMessage());
        }
    }

    // TODO: move the next two methods in an data access layer
    private void obtainExtraInformation(Video video) throws NmjEditorException {
        Connection connection = Application.getSqliteConnector().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_INFORMATION_QUERY + video.getId());
            while (rs.next()) {
                String attributeType = rs.getString("TYPE");
                if (attributeType.equals("KEYWORD")) {
                    video.addKeyword(rs.getString("VALUE"));
                } else if (attributeType.equals("GENRE")) {
                    video.addGenre(rs.getString("VALUE"));
                } else if (attributeType.equals("DIRECTOR")) {
                    video.addDirector(rs.getString("VALUE"));
                } else if (attributeType.equals("PRINCIPAL_CAST_MEMBER")) {
                    video.addCasting(rs.getString("VALUE"));
                }
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error retrieving information of video " + video.getTitle(), e);
        }
    }

    private List<Video> createVideoStructure(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_QUERY);
        List<Video> videos = new ArrayList<Video>();
        while (rs.next()) {
            Video video = new Video();
            video.setId(rs.getInt("VIDEO_ID"));
            video.setTitle(rs.getString("TITLE"));
            video.setReleaseDate(rs.getString("RELEASE_DATE"));
            video.setRuntime(formatIntoHHMMSS(rs.getInt("RUNTIME")));
            video.setRating(rs.getDouble("RATING"));
            video.setSystem(rs.getString("SYSTEM"));
            video.setVideoCodec(rs.getString("VIDEO_CODEC"));
            video.setResolution(rs.getString("RESOLUTION"));
            video.setFileName(rs.getString("PATH"));
            video.setFps(rs.getDouble("FPS"));
            videos.add(video);
        }
        return videos;
    }

    private String formatIntoHHMMSS(int secondsInput) {
        int hours = secondsInput / 3600, remainder = secondsInput % 3600, minutes = remainder / 60, seconds = remainder % 60;
        return ((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                + (seconds < 10 ? "0" : "") + seconds);

    }
}