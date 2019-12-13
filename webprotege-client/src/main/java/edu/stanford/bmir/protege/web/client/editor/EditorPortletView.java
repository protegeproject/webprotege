package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public interface EditorPortletView extends AcceptsOneWidget, IsWidget, HasDispose {

    interface EditorPaneChangedHandler {
        void handleEditorPaneChanged();
    }

    @Nonnull
    SimplePanel getTagListViewContainer();

    @Nonnull
    AcceptsOneWidget addPane(@Nonnull String displayName,
                             @Nonnull String additionalStyles);

    boolean isPaneVisible(@Nonnull String displayName);

    void setVisibleIndex(int index);

    void setEditorPaneChangedHandler(@Nonnull EditorPaneChangedHandler handler);


}
