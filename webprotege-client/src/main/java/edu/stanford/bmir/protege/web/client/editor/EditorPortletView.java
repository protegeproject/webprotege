package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public interface EditorPortletView extends IsWidget {

    @Nonnull
    SimplePanel getTagListViewContainer();

    @Nonnull
    AcceptsOneWidget addPane(@Nonnull String displayName,
                             @Nonnull String additionalStyles);

}
