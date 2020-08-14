package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public interface DictionaryLanguageSelectorView extends IsWidget {

    void requestFocus();

    interface TypeNameChangedHandler {
        void handleTypeNameChanged();
    }

    void setTypeNames(@Nonnull ImmutableList<String> typeNames);

    @Nonnull
    Optional<String> getSelectedTypeName();

    void setSelectedTypeName(@Nonnull String typeName);

    void setTypeNameChangedHandler(@Nonnull TypeNameChangedHandler handler);

    @Nonnull
    AcceptsOneWidget getTypeDetailsContainer();

    void clear();
}
