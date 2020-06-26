package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface NumberControlDescriptorView extends IsWidget {

    int getLength();

    void setLength(int length);

    void setFormat(@Nonnull String format);

    String getFormat();

    void setPlaceholder(LanguageMap placeholder);

    @Nonnull
    LanguageMap getPlaceholder();

    @Nonnull
    AcceptsOneWidget getRangeViewContainer();
}
