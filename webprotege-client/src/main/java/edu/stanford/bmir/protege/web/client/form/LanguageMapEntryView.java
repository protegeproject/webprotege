package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface LanguageMapEntryView extends IsWidget, HasRequestFocus, HasPlaceholder {

    void setLangTag(@Nonnull String langTag);

    @Nonnull
    String getLangTag();

    void setValue(@Nonnull String value);

    @Nonnull
    String getValue();

    void setValueChangedHandler(Consumer<String> valueConsumer);

    void setLangTagChangedHandler(Consumer<String> langTagConsumer);
}
