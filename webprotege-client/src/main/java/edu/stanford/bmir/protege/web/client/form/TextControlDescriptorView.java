package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface TextControlDescriptorView extends IsWidget {

    void setStringType(@Nonnull StringType stringType);

    @Nonnull
    StringType getStringType();

    void setLineMode(@Nonnull LineMode lineMode);

    @Nonnull
    LineMode getLineMode();

    void setPattern(@Nonnull String pattern);

    @Nonnull
    String getPattern();

    void setPatternViolationMessage(@Nonnull LanguageMap patternViolationMessage);

    @Nonnull
    LanguageMap getPatternViolationMessage();

    void setPlaceholder(@Nonnull LanguageMap languageMap);

    @Nonnull
    LanguageMap getPlaceholder();

}
