package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
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
public class AnnotationTranslator {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

    @Nonnull
    private final ContextRenderer renderer;

    @AutoFactory
    @Inject
    public AnnotationTranslator(@Provided  @Nonnull EntitiesInProjectSignatureByIriIndex entitiesIndex,
                                @Provided @Nonnull ContextRenderer renderer) {
        this.entitiesIndex = checkNotNull(entitiesIndex);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    public Set<PropertyValue> translate(@Nonnull OWLAnnotation annotation,
                                        @Nonnull State state) {
        if(annotation.getValue() instanceof IRI) {
            var entities = entitiesIndex.getEntityInSignature((IRI) annotation.getValue()).collect(Collectors.toSet());
            if(!entities.isEmpty()) {
                return entities
                        .stream()
                        .sorted()
                        .map(entity -> toPropertyValue(entity, annotation, state))
                        .collect(Collectors.toSet());
            }
        }
        return Collections.singleton(PropertyAnnotationValue.get(renderer.getAnnotationPropertyData(annotation.getProperty()), renderer.getAnnotationValueData(annotation.getValue()), State.ASSERTED));

    }

    private PropertyAnnotationValue toPropertyValue(@Nonnull OWLEntity entity,
                                                    @Nonnull OWLAnnotation annotation,
                                                    @Nonnull State state) {
        return PropertyAnnotationValue.get(renderer.getAnnotationPropertyData(annotation.getProperty()),
                                           renderer.getEntityData(entity),
                                           state);
    }
}
