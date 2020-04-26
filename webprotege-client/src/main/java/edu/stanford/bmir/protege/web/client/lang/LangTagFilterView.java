package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public interface LangTagFilterView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getLangTagsContainer();
}
