package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public interface AnnotationSimpleMatchingCriteriaView extends IsWidget, HasRequestFocus {

    boolean isMatchProperty();

    @Nonnull
    Optional<OWLAnnotationPropertyData> getProperty();

    boolean isMatchLexicalValue();

    @Nonnull
    String getLexicalValueExpression();

    boolean isLexicalValueRegEx();

    boolean isMatchLangTag();

    @Nonnull
    Optional<String> getLangTag();
}
