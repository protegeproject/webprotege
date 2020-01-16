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

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public FormFrameConverter(@Nonnull RenderingManager renderingManager) {
        this.renderingManager = renderingManager;
    }

    @Nonnull
    public Optional<EntityFrame<? extends OWLEntityData>> toEntityFrame(@Nonnull FormFrame formFrame) {

        return formFrame.getSubject()
                 .accept(new FormSubject.FormDataSubjectVisitorEx<>() {
                     @Override
                     public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull FormEntitySubject formDataEntitySubject) {
                         return getEntityFrame(formDataEntitySubject, formFrame);
                     }

                     @Override
                     public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull FormIriSubject formDataIriSubject) {
                         return Optional.empty();
                     }
                 });
    }

    public Optional<EntityFrame<? extends OWLEntityData>> getEntityFrame(@Nonnull FormEntitySubject formDataEntitySubject,
                                                                         @Nonnull FormFrame formFrame) {
        return formDataEntitySubject.getEntity()
                .accept(new OWLEntityVisitorEx<>() {
                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLClass cls) {
                        return Optional.of(ClassFrame.get(renderingManager.getClassData(cls),
                                                          formFrame.getClasses(),
                                                          formFrame.getPropertyValues()));
                    }

                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLObjectProperty property) {
                        return Optional.of(ObjectPropertyFrame.get(renderingManager.getObjectPropertyData(property),
                                                                   getAnnotationPropertyValues(formFrame),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of(),
                                                                   ImmutableSet.of()));
                    }

                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLDataProperty property) {
                        return Optional.of(DataPropertyFrame.get(renderingManager.getDataPropertyData(property),
                                                                 formFrame.getPropertyValues(),
                                                                 ImmutableSet.of(),
                                                                 ImmutableSet.of(),
                                                                 false));
                    }

                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLNamedIndividual individual) {
                        return Optional.of(NamedIndividualFrame.get(renderingManager.getIndividualData(individual),
                                                                    formFrame.getClasses(),
                                                                    formFrame.getPropertyValues(),
                                                                    ImmutableSet.of()));
                    }

                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLDatatype datatype) {
                        return Optional.empty();
                    }

                    @Nonnull
                    @Override
                    public Optional<EntityFrame<? extends OWLEntityData>> visit(@Nonnull OWLAnnotationProperty property) {
                        return Optional.of(AnnotationPropertyFrame.get(renderingManager.getAnnotationPropertyData(property),
                                                                       getAnnotationPropertyValues(formFrame),
                                                                       ImmutableSet.of(),
                                                                       ImmutableSet.of()));
                    }
                });
    }

    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues(@Nonnull FormFrame formFrame) {
        return formFrame.getPropertyValues()
        .stream()
        .filter(PropertyValue::isAnnotation)
        .map(pv -> (PropertyAnnotationValue) pv)
        .collect(toImmutableSet());
    }
}
