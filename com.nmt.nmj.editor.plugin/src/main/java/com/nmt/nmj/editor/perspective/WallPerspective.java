package com.nmt.nmj.editor.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.nmt.nmj.editor.view.WallView;

public class WallPerspective implements IPerspectiveFactory {

    public static final String ID = "com.nmt.nmj.editor.plugin.wallPerspective";

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(WallView.ID, false, IPageLayout.RIGHT, 0.25f, editorArea);

        layout.addPerspectiveShortcut("Wall");

    }
}
