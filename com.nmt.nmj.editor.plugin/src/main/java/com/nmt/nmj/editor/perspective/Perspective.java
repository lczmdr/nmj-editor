package com.nmt.nmj.editor.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.nmt.nmj.editor.view.EditorView;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(EditorView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
    }
}
