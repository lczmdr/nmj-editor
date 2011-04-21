package com.nmt.nmj.editor.view.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.nmt.nmj.editor.model.Video;

public class VideoContentProvider implements IStructuredContentProvider {

    public Object[] getElements(Object obj) {
        List<Video> videos = (ArrayList<Video>) obj;
        return videos.toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void formChanged() {
    }

}
