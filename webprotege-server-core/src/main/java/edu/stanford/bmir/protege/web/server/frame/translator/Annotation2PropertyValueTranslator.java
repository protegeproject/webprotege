package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-14
 */
public class Annotation2PropertyValueTranslator {

    @AutoFactory
    @Inject
    public Annotation2PropertyValueTranslator() {
    }

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLAnnotation annotation,
                                             @Nonnull State state) {
        return Collections.singleton(PlainPropertyAnnotationValue.get(annotation.getProperty(),
                                                                      annotation.getValue(),
                                                                      state));

    }
}
