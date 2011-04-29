package com.nmt.nmj.editor.view.components;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nmt.nmj.editor.ImageResource;
import com.nmt.nmj.editor.dialog.CalendarDialog;
import com.nmt.nmj.editor.model.Video;
import com.nmt.nmj.editor.nls.NlsMessages;
import com.nmt.nmj.editor.view.ListView;

public class MovieInformationComposite extends Composite {

    private Canvas posterImageCanvas;
    protected Image posterImage;
    private Button updateMovieButton;
    private IWorkbenchWindow window;
    private Composite detailedInformationComposite;
    private Label movieTitle;
    private Label fileNameLabel;
    private Text certificationMpaaText;
    private Text imdbText;
    private Text searchTitle;
    private Button movieTypeButton;
    private Button tvSerieTypeButton;
    private List genresList;
    private List directorsList;
    private List castingList;
    private List keywordsList;
    private Text synopsisText;
    private Text releaseDateText;
    private Video currentVideo;

    public MovieInformationComposite(Composite parent, int style) {
        super(parent, style);
        window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }

    public void initialize() {
        Group detailedInformationGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
        detailedInformationGroup.setText(NlsMessages.movie_information_group_title);
        detailedInformationGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        detailedInformationGroup.setLayout(new GridLayout(1, false));

        detailedInformationComposite = new Composite(detailedInformationGroup, SWT.NONE);
        detailedInformationComposite.setLayout(new GridLayout(3, false));
        detailedInformationComposite.setVisible(false);

        GridData gd = new GridData();
        gd.horizontalSpan = 2;

        movieTitle = new Label(detailedInformationComposite, SWT.WRAP);
        movieTitle.setLayoutData(gd);
        Font boldFont = new Font(window.getShell().getDisplay(), "", 14, SWT.BOLD); //$NON-NLS-1$
        movieTitle.setFont(boldFont);
        movieTitle.setToolTipText(NlsMessages.movie_information_movie_title_click);
        movieTitle.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                InputDialog changeMovieTitle = new InputDialog(window.getShell(),
                        NlsMessages.movie_information_movie_title,
                        NlsMessages.movie_information_new_movie_title, movieTitle.getText(),
                        new IInputValidator() {
                            @Override
                            public String isValid(String newText) {
                                if (newText.trim().length() == 0) {
                                    return NlsMessages.movie_information_empty_value_not_allowed;
                                }
                                return null;
                            }
                        });
                if (changeMovieTitle.open() == Window.OK) {
                    movieTitle.setText(changeMovieTitle.getValue().trim());
                }
            };
        });
        movieTitle.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                movieTitle.setCursor(new Cursor(window.getShell().getDisplay(), SWT.CURSOR_HAND));
            }
        });

        updateMovieButton = new Button(detailedInformationComposite, SWT.PUSH);
        updateMovieButton.setImage(new Image(window.getShell().getDisplay(), ListView.class
                .getResourceAsStream(ImageResource.SAVE_IMAGE))); //$NON-NLS-1$
        updateMovieButton.setText(NlsMessages.movie_information_save);

        fileNameLabel = new Label(detailedInformationComposite, SWT.WRAP);
        fileNameLabel.setText(NlsMessages.movie_information_filename);
        fileNameLabel.pack();

        gd = new GridData();
        gd.horizontalSpan = 3;
        fileNameLabel.setLayoutData(gd);

        Composite doubleColumnComposite = new Composite(detailedInformationComposite, SWT.NONE);
        doubleColumnComposite.setLayout(new GridLayout(2, false));

        Composite basicInformationComposite = new Composite(doubleColumnComposite, SWT.NONE);
        basicInformationComposite.setLayout(new GridLayout(1, false));

        createReleaseDateWidget(basicInformationComposite);

        Composite certificationImdbComposite = new Composite(basicInformationComposite, SWT.NONE);
        certificationImdbComposite.setLayout(new GridLayout(2, false));

        Label l = new Label(certificationImdbComposite, SWT.NONE);
        l.setText(NlsMessages.movie_information_certification);

        certificationMpaaText = new Text(certificationImdbComposite, SWT.BORDER);
        gd = new GridData();
        gd.widthHint = 100;
        certificationMpaaText.setLayoutData(gd);

        l = new Label(certificationImdbComposite, SWT.NONE);
        l.setText(NlsMessages.movie_information_imdb);

        imdbText = new Text(certificationImdbComposite, SWT.BORDER);
        gd = new GridData();
        gd.widthHint = 100;
        imdbText.setLayoutData(gd);

        l = new Label(certificationImdbComposite, SWT.NONE);
        l.setText(NlsMessages.movie_information_search_title_text);
        searchTitle = new Text(certificationImdbComposite, SWT.BORDER);
        gd = new GridData();
        gd.widthHint = 150;
        searchTitle.setLayoutData(gd);

        Group videoTypeGroup = new Group(basicInformationComposite, SWT.SHADOW_ETCHED_IN);
        videoTypeGroup.setText(NlsMessages.movie_information_video_type);
        videoTypeGroup.setLayout(new GridLayout(2, false));

        movieTypeButton = new Button(videoTypeGroup, SWT.RADIO);
        movieTypeButton.setText(NlsMessages.common_movie);

        tvSerieTypeButton = new Button(videoTypeGroup, SWT.RADIO);
        tvSerieTypeButton.setText(NlsMessages.common_tv_show);

        Composite composite = new Composite(doubleColumnComposite, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        genresList = createListControl(composite, NlsMessages.movie_information_genres,
                NlsMessages.movie_information_genre);
        directorsList = createListControl(composite, NlsMessages.movie_information_directors,
                NlsMessages.movie_information_director);
        castingList = createListControl(composite, NlsMessages.movie_information_casting,
                NlsMessages.movie_information_casting_lowercase);
        keywordsList = createListControl(composite, NlsMessages.movie_information_keywords,
                NlsMessages.movie_information_keyword_lowercase);

        Composite sinopsisComposite = new Composite(doubleColumnComposite, SWT.NONE);
        sinopsisComposite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        sinopsisComposite.setLayoutData(gridData);

        Label label = new Label(sinopsisComposite, SWT.NONE);
        label.setText(NlsMessages.movie_information_synopsis);
        synopsisText = new Text(sinopsisComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        synopsisText.setLayoutData(new GridData(500, 70));

        TabFolder tabFolder = new TabFolder(detailedInformationComposite, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 240;
        gd.verticalSpan = 2;
        tabFolder.setLayoutData(gd);

        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText(NlsMessages.movie_information_poster);

        Composite imageCanvasComposite = new Composite(tabFolder, SWT.BORDER);
        imageCanvasComposite.setLayout(new GridLayout(1, false));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 180;
        gd.widthHint = 140;
        imageCanvasComposite.setLayoutData(gd);

        posterImageCanvas = new Canvas(imageCanvasComposite, SWT.NO_REDRAW_RESIZE);
        posterImageCanvas.setLayoutData(new GridData(124, 183));
        posterImageCanvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.drawImage(posterImage, 0, 0);
            }
        });

        Button changeImageButton = new Button(imageCanvasComposite, SWT.PUSH);
        changeImageButton.setText(NlsMessages.movie_information_change_picture);
        changeImageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

            }
        });

        tabItem.setControl(imageCanvasComposite);

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText(NlsMessages.movie_information_thumbnail);

        tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText(NlsMessages.movie_information_wallpaper);

    }

    private org.eclipse.swt.widgets.List createListControl(Composite parent, String label, final String type) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));

        Label l = new Label(composite, SWT.NONE);
        l.setText(label);
        l.pack();

        final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.V_SCROLL);
        GridData gd = new GridData(SWT.BORDER);
        gd.heightHint = 70;
        gd.widthHint = 120;
        list.setLayoutData(gd);

        Composite buttons = new Composite(composite, SWT.NONE);
        buttons.setLayout(new GridLayout(1, false));

        Button addButton = new Button(buttons, SWT.PUSH);
        Display display = buttons.getDisplay();
        addButton.setImage(new Image(display, ListView.class.getResourceAsStream(ImageResource.ADD_IMAGE))); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog input = new InputDialog(window.getShell(),
                        NlsMessages.movie_information_input,
                        NlsMessages.movie_information_new + " " + type, "", //$NON-NLS-3$
                        new IInputValidator() {
                            @Override
                            public String isValid(String newText) {
                                if (newText.trim().length() == 0) {
                                    return NlsMessages.movie_information_empty_value_not_allowed;
                                }
                                return null;
                            }
                        });
                if (input.open() == Window.OK) {
                    String value = input.getValue();
                    list.add(value.trim());
                }
            }
        });

        Button removeButton = new Button(buttons, SWT.PUSH);
        removeButton.setImage(new Image(display, ListView.class.getResourceAsStream(ImageResource.REMOVE_IMAGE)));
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (list.getSelectionIndex() != -1) {
                    list.remove(list.getSelectionIndex());
                    list.setFocus();
                }
            }
        });
        return list;
    }

    private void createReleaseDateWidget(Composite parent) {
        Composite releaseDateContainer = new Composite(parent, SWT.NONE);
        releaseDateContainer.setLayout(new GridLayout(3, false));

        Label l = new Label(releaseDateContainer, SWT.NONE);
        l.setText(NlsMessages.movie_information_release_date);
        l.pack();

        releaseDateText = new Text(releaseDateContainer, SWT.BORDER);
        releaseDateText.setEditable(false);
        GridData gd = new GridData();
        gd.widthHint = 80;
        releaseDateText.setLayoutData(gd);

        Button openCalendar = new Button(releaseDateContainer, SWT.PUSH);
        openCalendar.setText("..."); //$NON-NLS-1$
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

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        detailedInformationComposite.setVisible(visible);
    }

    public void showMovieInformation(Video currentVideo) {
        this.currentVideo = currentVideo;
        movieTitle.setText(StringEscapeUtils.unescapeHtml(currentVideo.getTitle()));
        GridData layoutData = new GridData();
        layoutData.heightHint = 20;
        movieTitle.setLayoutData(layoutData);
        movieTitle.pack();
        searchTitle.setText(StringEscapeUtils.unescapeHtml(currentVideo.getSearchTitle()));
        releaseDateText.setText(currentVideo.getReleaseDate());
        fileNameLabel.setText(NlsMessages.movie_information_filename + currentVideo.getFileName());
        fileNameLabel.pack();
        synopsisText.setText(StringEscapeUtils.unescapeHtml(currentVideo.getSynopsis()));
        certificationMpaaText.setText(currentVideo.getCertification());
        imdbText.setText(currentVideo.getImdb());
        movieTypeButton.setSelection(true);
        tvSerieTypeButton.setSelection(false);
        fillInformationList(currentVideo.getGenres(), genresList);
        fillInformationList(currentVideo.getDirectors(), directorsList);
        fillInformationList(currentVideo.getCasting(), castingList);
        fillInformationList(currentVideo.getKeywords(), keywordsList);
        if (!currentVideo.getPosterImage().equals("")) { //$NON-NLS-1$
            if (new File(currentVideo.getPosterImage()).exists()) {
                posterImage = new Image(window.getShell().getDisplay(), currentVideo.getPosterImage());
                posterImageCanvas.setVisible(true);
                posterImageCanvas.redraw();
            } else {
                posterImage = new Image(window.getShell().getDisplay(),
                        ListView.class.getResourceAsStream(ImageResource.NO_POSTER_IMAGE)); //$NON-NLS-1$
                posterImageCanvas.setVisible(true);
            }
        } else {
            posterImage = new Image(window.getShell().getDisplay(),
                    ListView.class.getResourceAsStream(ImageResource.NO_POSTER_IMAGE)); //$NON-NLS-1$
            posterImageCanvas.setVisible(true);
        }
    }

    private void fillInformationList(java.util.List<String> information, org.eclipse.swt.widgets.List informationList) {
        informationList.removeAll();
        for (String info : information) {
            informationList.add(info);
        }
    }

}
