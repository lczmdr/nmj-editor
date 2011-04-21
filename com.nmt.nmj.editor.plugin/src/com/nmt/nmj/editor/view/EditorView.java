package com.nmt.nmj.editor.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.ViewPart;

import com.nmt.nmj.editor.Application;
import com.nmt.nmj.editor.model.Video;

public class EditorView extends ViewPart {

    public static final String ID = "com.nmt.nmj.editor.editorView";

    private final String VIDEO_ID = "ID";
    private final String VIDEO_NAME = "Name";
    private final String VIDEO_TYPE = "Type";

    private String[] columnNames = new String[] { VIDEO_ID, VIDEO_NAME, VIDEO_TYPE };

    private Composite container;
    private Table table;
    private TableViewer tableViewer;

    private Label informationLabel;

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

        Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);

        informationLabel = new Label(container, SWT.WRAP);
        informationLabel.setText("Videos");
        informationLabel.setFont(boldFont);

        createVideoTable();

    }

    private void createVideoTable() {
        this.table = new Table(this.container, SWT.BORDER | SWT.MULTI | SWT.SCROLL_LINE | SWT.SINGLE
                | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        this.table.setHeaderVisible(true);
        this.table.setLinesVisible(true);

        TableColumn tableColumn = new TableColumn(this.table, SWT.CENTER);
        tableColumn.setText("ID");
        tableColumn.setWidth(70);

        tableColumn = new TableColumn(this.table, SWT.CENTER);
        tableColumn.setText("Name");
        tableColumn.setWidth(300);

        tableColumn = new TableColumn(this.table, SWT.CENTER);
        tableColumn.setText("Type");
        tableColumn.pack();

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 320;
        this.table.setLayoutData(gd);

        tableViewer = new TableViewer(table);
        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(columnNames);

        tableViewer.setContentProvider(new RepeticionContentProvider());
        tableViewer.setLabelProvider(new RepeticionLabelProvider());

        this.table.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                if (e.button == 1) {
                    System.out.println("selected ");
                }
            }
        });
    }

    class RepeticionContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object obj) {
            List<Video> videos = (ArrayList<Video>) obj;
            return videos.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void formChanged() {
            tableViewer.remove(table.getItems());
            tableViewer.add(this.getElements(null));
            tableViewer.refresh();
        }

    }

    class RepeticionLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getText(Object obj) {
            return "";
        }

        public Image getImage(Object obj) {
            return ((IWorkbenchAdapter) obj).getImageDescriptor(obj).createImage();
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object obj, int columnIndex) {
            Video video = (Video) obj;
            String result = "";

            switch (columnIndex) {
            case 0: // Video ID
                result = String.valueOf(video.getId());
                break;
            case 1: // Video Title
                result = video.getTitle();
                break;
            case 2: // Video Type
                result = video.getType();
                break;
            default:
                break;
            }
            return result;
        }
    }

    public void setFocus() {
    }

    public void refresh() {
        Connection connection = Application.getSqliteConnector().getConnection();
        try {
            if (!connection.isClosed()) {
                String fileName = Application.getSqliteConnector().getFileName();
                informationLabel.setText("Videos - Current database: " + fileName);
                List<Video> videos = createVideoStructure(connection);
                this.tableViewer.setInput(videos);
                container.layout();
            } else {
                informationLabel.setText("Videos");
                tableViewer.setInput(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Video> createVideoStructure(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT * FROM VIDEO");
            List<Video> videos = new ArrayList<Video>();
            while (rs.next()) {
                System.out.println("name = " + rs.getString("TITLE"));
                Video video = new Video();
                video.setId(rs.getInt("VIDEO_ID"));
                video.setTitle(rs.getString("TITLE"));
                video.setType(rs.getString("TITLE_TYPE"));
                videos.add(video);
            }
            return videos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}