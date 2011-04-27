package com.nmt.nmj.editor.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.nmt.nmj.editor.view.ListView;

public class ListPerspective implements IPerspectiveFactory {

    public static final String ID = "com.nmt.nmj.editor.plugin.listPerspective";

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(ListView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);

        layout.addPerspectiveShortcut("List");

    }
}
