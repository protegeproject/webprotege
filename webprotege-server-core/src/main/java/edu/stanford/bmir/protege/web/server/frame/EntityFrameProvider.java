package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.PlainEntityFrame;
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
    public PlainEntityFrame getEntityFrame(@Nonnull OWLEntity entity,
                                           boolean includeDerivedInfo) {
        return entity.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLClass cls) {
                return classFrameTranslator.getFrame(cls);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLObjectProperty property) {
                return objectPropertyFrameTranslator.getFrame(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDataProperty property) {
                return dataPropertyFrameTranslator.getFrame(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLNamedIndividual individual) {
                return namedIndividualFrameTranslator.getFrame(individual, includeDerivedInfo);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDatatype datatype) {
                throw new RuntimeException("Datatypes are not supported");
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLAnnotationProperty property) {
                return annotationPropertyFrameTranslator.getFrame(property);
            }
        });
    }
}
