package com.nmt.nmj.editor.view.sorter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.TableColumn;

import com.nmt.nmj.editor.model.Video;

public class MovieSorter extends ViewerSorter {

    private TableColumn currentColumn = null;
    private Map<TableColumn, Boolean> sortMap = new HashMap<TableColumn, Boolean>();

    public MovieSorter(TableColumn defaultColumn) {
        setCurrentColumn(defaultColumn);
    }

    public void pushSortCriteria(TableColumn column) {
        if (this.sortMap.get(column) == null) {
            this.sortMap.put(column, new Boolean(true));
        } else {
            boolean newSort = !((Boolean) this.sortMap.get(column)).booleanValue();
            this.sortMap.put(column, new Boolean(newSort));
        }
    }

    public boolean isDescending(TableColumn column) {
        boolean returnValue = true;
        if (this.sortMap.get(column) != null)
            returnValue = ((Boolean) this.sortMap.get(column)).booleanValue();
        else
            pushSortCriteria(column);
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        Video video1 = (Video) obj1;
        Video video2 = (Video) obj2;
        if (this.currentColumn == ((TableViewer) viewer).getTable().getColumn(0)) {
            Comparator<String> comparator = getComparator();
            if (isDescending(this.currentColumn)) {
                return comparator.compare(String.valueOf(video1.getId()), String.valueOf(video2.getId()));
            }
            return comparator.compare(String.valueOf(video2.getId()), String.valueOf(video1.getId()));
        } else if (this.currentColumn == ((TableViewer) viewer).getTable().getColumn(1)) {
            Comparator<String> comparator = getComparator();
            if (isDescending(this.currentColumn)) {
                return comparator.compare(video1.getTitle(), video2.getTitle());
            }
            return comparator.compare(video2.getTitle(), video1.getTitle());
        }
        return 0;
    }

    public void setCurrentColumn(TableColumn currentColumn) {
        this.currentColumn = currentColumn;
        pushSortCriteria(currentColumn);
    }

}