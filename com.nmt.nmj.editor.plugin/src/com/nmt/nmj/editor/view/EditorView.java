package com.nmt.nmj.editor.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
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

    private String[] columnNames = new String[] { VIDEO_ID, VIDEO_TITLE, VIDEO_RELEASE_DATE, VIDEO_RUNTIME, VIDEO_RATING,
            VIDEO_SYSTEM, VIDEO_CODEC, VIDEO_DIMENSIONS, VIDEO_FPS };

    private Composite container;
    private Table videoTable;
    private TableViewer videoTableViewer;

    private Label informationLabel;

    private VideoSorter videoTableSorter;

    public void createPartControl(Composite parent) {

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

        Group group = new Group(container, SWT.SHADOW_ETCHED_IN);
        group.setText("Detailed information");
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label l = new Label(group, SWT.WRAP);
        l.setText("Video information area");

    }

    private Control createVideoTable(TabFolder tabFolder) {
        Composite composite = new Composite(tabFolder, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        this.videoTable = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.SCROLL_LINE | SWT.SINGLE
                | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        this.videoTable.setHeaderVisible(true);
        this.videoTable.setLinesVisible(true);

        TableColumn idTableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        idTableColumn.setText(VIDEO_ID);
        idTableColumn.setWidth(70);
        idTableColumn.setResizable(false);
        idTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn titleTableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        titleTableColumn.setText(VIDEO_TITLE);
        titleTableColumn.setWidth(300);
        titleTableColumn.addListener(SWT.Selection, this.selChangeListenerColumn);

        TableColumn tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RELEASE_DATE);
        tableColumn.setWidth(120);
        tableColumn.setResizable(false);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RUNTIME);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_RATING);
        tableColumn.setWidth(80);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_SYSTEM);
        tableColumn.setWidth(120);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_CODEC);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_DIMENSIONS);
        tableColumn.setWidth(100);

        tableColumn = new TableColumn(this.videoTable, SWT.CENTER);
        tableColumn.setText(VIDEO_FPS);
        tableColumn.setWidth(100);

        videoTableViewer = new TableViewer(videoTable);
        videoTableViewer.setUseHashlookup(true);
        videoTableViewer.setColumnProperties(columnNames);

        videoTableViewer.setContentProvider(new VideoContentProvider());
        videoTableViewer.setLabelProvider(new VideoLabelProvider());

        videoTableSorter = new VideoSorter(titleTableColumn);
        videoTableViewer.setSorter(videoTableSorter);

        this.videoTable.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                if (e.button == 1) {
                    IStructuredSelection selection = (IStructuredSelection) videoTableViewer.getSelection();
                    Video video = (Video) selection.getFirstElement();
                    System.out.println("selected " + video.getTitle());
                }
            }
        });
        return composite;
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
                List<Video> videos = createVideoStructure(connection);
                this.videoTableViewer.setInput(videos);
                container.layout();
            } else {
                informationLabel.setText("Current Database: ");
                videoTableViewer.setInput(null);
            }
        } catch (SQLException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    "Error reading database", "An error happened trying to read the database: \n" + e.getMessage());
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
            video.setYear(rs.getString("RELEASE_DATE"));
            video.setRuntime(formatIntoHHMMSS(rs.getInt("RUNTIME")));
            video.setRating(rs.getInt("RATING"));
            video.setSystem(rs.getString("SYSTEM"));
            video.setVideoCodec(rs.getString("VIDEO_CODEC"));
            video.setResolution(rs.getString("RESOLUTION"));
            video.setFps(rs.getInt("FPS"));
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