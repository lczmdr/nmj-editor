package com.nmt.nmj.editor.dialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.moviejukebox.themoviedb.model.Artwork;
import com.moviejukebox.themoviedb.model.MovieDB;
import com.nmt.nmj.editor.backend.MovieInformationFactory;
import com.nmt.nmj.editor.backend.TmdbService;
import com.nmt.nmj.editor.model.Video;
import com.nmt.nmj.editor.nls.NlsMessages;

public class MovieImagesDialog extends Dialog {

    private static final int CHOOSE = 0;

    private Map<String, Image> imagesCache;
    private String selectedImage;
    private Video video;
    private TmdbService tmdbService;
    private List<Artwork> movieImages;
    private Combo moviesCombo;
    private Canvas imageCanvas;
    private Image image;
    private Button previousButton;
    private Button nextButton;
    private Text movieTitle;
    private Button searchMoviesButton;
    private Label imagesCount;
    private int totalImages;
    private int currentImage;

    public MovieImagesDialog(Shell parentShell, Video video) {
        super(parentShell);
        this.video = video;
        tmdbService = MovieInformationFactory.createTmdbService();
    }

    protected void configureShell(Shell newShell) {
        newShell.setText("Movie images from TMDB");
        super.configureShell(newShell);
    }

    protected Control createDialogArea(final Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite searchMovieComposite = new Composite(parent, SWT.NONE);
        searchMovieComposite.setLayout(new GridLayout(3, false));

        Label l = new Label(searchMovieComposite, SWT.NONE);
        l.setText("Movie title:");

        movieTitle = new Text(searchMovieComposite, SWT.BORDER);
        GridData gd = new GridData();
        gd.widthHint = 200;
        movieTitle.setLayoutData(gd);
        movieTitle.setText(video.getTitle());

        searchMoviesButton = new Button(searchMovieComposite, SWT.PUSH);
        searchMoviesButton.setText("Search");
        searchMoviesButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (movieTitle.getText().trim().length() == 0) {
                    MessageDialog.openError(parent.getShell(), NlsMessages.common_error,
                            NlsMessages.movie_information_empty_value_not_allowed);
                    return;
                }
                searchMovie(movieTitle.getText().trim());
            }
        });

        l = new Label(searchMovieComposite, SWT.NONE);
        l.setText("Available movies:");

        moviesCombo = new Combo(searchMovieComposite, SWT.READ_ONLY);
        moviesCombo.setLayoutData(gd);
        moviesCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                searchImages();
            }

        });

        imageCanvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE);
        imageCanvas.setLayoutData(new GridData(124, 183));
        imageCanvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (image != null) {
                    e.gc.drawImage(image, 0, 0);
                }
            }
        });

        Composite navigationControl = new Composite(parent, SWT.NONE);
        navigationControl.setLayout(new GridLayout(2, true));
        navigationControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        previousButton = new Button(navigationControl, SWT.PUSH);
        previousButton.setText("Previous");
        previousButton.setEnabled(false);
        previousButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentImage-- == 2) {
                    previousButton.setEnabled(false);
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(true);
                }
                renderImage();
            }
        });

        nextButton = new Button(navigationControl, SWT.PUSH);
        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentImage++ == totalImages - 1) {
                    nextButton.setEnabled(false);
                    previousButton.setEnabled(true);
                } else {
                    previousButton.setEnabled(true);
                }
                renderImage();
            }
        });

        imagesCount = new Label(parent, SWT.NONE);
        imagesCount.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        searchMovie(video.getSearchTitle());

        return parent;
    }

    private void searchMovie(String movieTitle) {
        List<MovieDB> movies = tmdbService.searchMovie(movieTitle);
        moviesCombo.removeAll();
        for (MovieDB movie : movies) {
            moviesCombo.add(movie.getId() + " - " + movie.getTitle());
        }
    }

    protected void searchImages() {
        if (moviesCombo.getSelectionIndex() != -1) {
            String movieId = moviesCombo.getItem(moviesCombo.getSelectionIndex());
            movieId = movieId.substring(0, movieId.indexOf("-")).trim();
            List<Artwork> movieImages = tmdbService.obtainImages(movieId);
            this.movieImages = new ArrayList<Artwork>();
            for (Artwork artwork : movieImages) {
                if (artwork.getType().equals(Artwork.ARTWORK_TYPE_POSTER)
                        && artwork.getSize().equals(Artwork.ARTWORK_SIZE_COVER)) {
                    this.movieImages.add(artwork);
                }
            }
            imagesCount.setText(this.movieImages.size() + " available images");
            currentImage = totalImages = 0;
            imagesCache = new HashMap<String, Image>();
            if (movieImages.size() > 0) {
                previousButton.setEnabled(false);
                nextButton.setEnabled(true);
                totalImages = this.movieImages.size();
                currentImage = 1;
                renderImage();
            }
        }
    }

    protected void createButtonsForButtonBar(Composite parent) {
        this.createButton(parent, CHOOSE, NlsMessages.common_choose, false);
        this.createButton(parent, CANCEL, NlsMessages.common_cancel, false);
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    private void renderImage() {
        if (imagesCache.containsKey(movieImages.get(currentImage - 1).getUrl())) {
            image = imagesCache.get(movieImages.get(currentImage - 1).getUrl());
        } else {
            try {
                URL url = new URL(movieImages.get(currentImage - 1).getUrl());
                InputStream is = url.openStream();
                image = new Image(getShell().getDisplay(), is);
                imagesCache.put(movieImages.get(currentImage - 1).getUrl(), image);
            } catch (MalformedURLException e1) {
                MessageDialog.openError(getShell(), NlsMessages.common_error, "Unknown URL");
            } catch (IOException e2) {
                MessageDialog.openError(getShell(), NlsMessages.common_error, "Error reading the image file");
            }
        }
        imageCanvas.redraw();
    }

}
