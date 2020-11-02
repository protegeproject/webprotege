package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public interface IncrementingPatternDescriptorView extends IsWidget {

    void setStartingValue(int startingValue);

    void setFormat(@Nonnull String format);

    void clear();

    int getStartingValue();

    String getFormat();
}
