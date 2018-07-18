package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public interface DictionaryLanguageDataView extends IsWidget, HasRequestFocus {

    void clear();

    void clearAnnotationProperty();

    void setAnnotationProperty(@Nonnull OWLAnnotationPropertyData annotationProperty);

    @Nonnull
    Optional<OWLAnnotationPropertyData> getAnnotationProperty();

    @Nonnull
    String getLang();

    void setLang(@Nonnull String lang);

}
