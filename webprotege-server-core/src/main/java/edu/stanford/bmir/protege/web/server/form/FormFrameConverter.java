package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormIriSubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public class FormFrameConverter {

    @Inject
    public FormFrameConverter() {
    }

    @Nonnull
    public Optional<PlainEntityFrame> toEntityFrame(@Nonnull FormFrame formFrame) {

        return formFrame.getSubject()
                 .accept(new FormSubject.FormDataSubjectVisitorEx<>() {
                     @Override
                     public Optional<PlainEntityFrame> visit(@Nonnull FormEntitySubject formDataEntitySubject) {
                         return getEntityFrame(formDataEntitySubject, formFrame);
                     }

                     @Override
                     public Optional<PlainEntityFrame> visit(@Nonnull FormIriSubject formDataIriSubject) {
                         return Optional.empty();
                     }
                 });
    }

    public Optional<PlainEntityFrame> getEntityFrame(@Nonnull FormEntitySubject formDataEntitySubject,
                                                                         @Nonnull FormFrame formFrame) {
        return formDataEntitySubject.getEntity()
                .accept(new OWLEntityVisitorEx<>() {
                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLClass cls) {
                        return Optional.of(PlainClassFrame.get(cls,
                                                          formFrame.getClasses(),
                                                          formFrame.getPropertyValues()));
                    }

                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLObjectProperty property) {
                        return Optional.of(PlainObjectPropertyFrame.get(property,
                                                                   getAnnotationPropertyValues(formFrame),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of()));
                    }

                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLDataProperty property) {
                        return Optional.of(PlainDataPropertyFrame.get(property,
                                                                 getAnnotationPropertyValues(formFrame),
                                                                 ImmutableSet.of(),
                                                                 ImmutableSet.of(),
                                                                 false));
                    }

                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLNamedIndividual individual) {
                        return Optional.of(PlainNamedIndividualFrame.get(individual,
                                                                    formFrame.getClasses(), ImmutableSet.<OWLNamedIndividual>of(),
                                                                         formFrame.getPropertyValues()));
                    }

                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLDatatype datatype) {
                        return Optional.empty();
                    }

                    @Nonnull
                    @Override
                    public Optional<PlainEntityFrame> visit(@Nonnull OWLAnnotationProperty property) {
                        return Optional.of(PlainAnnotationPropertyFrame.get(property,
                                                                       getAnnotationPropertyValues(formFrame),
                                                                       ImmutableSet.of(),
                                                                       ImmutableSet.of()));
                    }
                });
    }

    public ImmutableSet<PlainPropertyAnnotationValue> getAnnotationPropertyValues(@Nonnull FormFrame formFrame) {
        return formFrame.getPropertyValues()
        .stream()
        .filter(PlainPropertyValue::isAnnotation)
        .map(pv -> (PlainPropertyAnnotationValue) pv)
        .collect(toImmutableSet());
    }
}
