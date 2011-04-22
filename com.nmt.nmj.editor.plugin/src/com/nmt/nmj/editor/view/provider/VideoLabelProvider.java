package com.nmt.nmj.editor.view.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.nmt.nmj.editor.model.Video;

public class VideoLabelProvider extends LabelProvider implements ITableLabelProvider {

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
        case 2: // Video Release Date
            result = video.getReleaseDate();
            break;
        case 3: // Video Runtime
            result = video.getRuntime();
            break;
        case 4: // Video Rating
            result = String.valueOf(video.getRating());
            break;
        case 5: // Video System
            result = video.getSystem();
            break;
        case 6: // Video Codec
            result = video.getVideoCodec();
            break;
        case 7: // Video Dimensions
            result = video.getResolution();
            break;
        case 8: // Video Fps
            result = String.valueOf(video.getFps());
            break;
        default:
            break;
        }
        return result;
    }
}