package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.ObjectPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
public class EntityFrameProvider {

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final ObjectPropertyFrameTranslator objectPropertyFrameTranslator;

    @Nonnull
    private final DataPropertyFrameTranslator dataPropertyFrameTranslator;

    @Nonnull
    private final AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @Inject
    public EntityFrameProvider(@Nonnull ClassFrameTranslator classFrameTranslator,
                               @Nonnull ObjectPropertyFrameTranslator objectPropertyFrameTranslator,
                               @Nonnull DataPropertyFrameTranslator dataPropertyFrameTranslator,
                               @Nonnull AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator,
                               @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator) {
        this.classFrameTranslator = classFrameTranslator;
        this.objectPropertyFrameTranslator = objectPropertyFrameTranslator;
        this.dataPropertyFrameTranslator = dataPropertyFrameTranslator;
        this.annotationPropertyFrameTranslator = annotationPropertyFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
    }

    @Nonnull
    public EntityFrame<? extends OWLEntityData> getEntityFrame(@Nonnull OWLEntity entity,
                                                               boolean includeDerivedInfo) {
        return entity.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLClass cls) {
                return classFrameTranslator.getFrame(
                        OWLClassData.get(cls, "", ImmutableMap.of())
                );
            }

            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLObjectProperty property) {
                return objectPropertyFrameTranslator.getFrame(
                        OWLObjectPropertyData.get(property, "", ImmutableMap.of())
                );
            }

            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLDataProperty property) {
                return dataPropertyFrameTranslator.getFrame(
                        OWLDataPropertyData.get(property, "", ImmutableMap.of())
                );
            }

            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLNamedIndividual individual) {
                return namedIndividualFrameTranslator.getFrame(
                        OWLNamedIndividualData.get(individual, "", ImmutableMap.of()),
                        includeDerivedInfo
                );
            }

            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLDatatype datatype) {
                throw new RuntimeException("Datatypes are not supported");
            }

            @Nonnull
            @Override
            public EntityFrame<? extends OWLEntityData> visit(@Nonnull OWLAnnotationProperty property) {
                return annotationPropertyFrameTranslator.getFrame(
                        OWLAnnotationPropertyData.get(property, "", ImmutableMap.of())
                );
            }
        });
    }
}
