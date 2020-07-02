package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.translator.*;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
@ProjectSingleton
public class FrameChangeGeneratorFactory {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final OntologyAxiomsIndex ontologyAxiomsIndex;

    @Nonnull
    private final ObjectPropertyFrameTranslator objectPropertyFrameTranslator;

    @Nonnull
    private final DataPropertyFrameTranslator dataPropertyFrameTranslator;

    @Nonnull
    private final AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassFrameProvider classFrameProvider;

    @Nonnull
    private final ClassFrame2FrameAxiomsTranslator classFrame2FrameAxiomsTranslator;

    @Inject
    public FrameChangeGeneratorFactory(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                       @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                       @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                       @Nonnull OntologyAxiomsIndex ontologyAxiomsIndex,
                                       @Nonnull ObjectPropertyFrameTranslator objectPropertyFrameTranslator,
                                       @Nonnull DataPropertyFrameTranslator dataPropertyFrameTranslator,
                                       @Nonnull AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator,
                                       @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull ClassFrameProvider classFrameProvider,
                                       @Nonnull ClassFrame2FrameAxiomsTranslator classFrame2FrameAxiomsTranslator) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.ontologyAxiomsIndex = ontologyAxiomsIndex;
        this.objectPropertyFrameTranslator = objectPropertyFrameTranslator;
        this.dataPropertyFrameTranslator = dataPropertyFrameTranslator;
        this.annotationPropertyFrameTranslator = annotationPropertyFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
        this.renderingManager = renderingManager;
        this.classFrameProvider = classFrameProvider;
        this.classFrame2FrameAxiomsTranslator = classFrame2FrameAxiomsTranslator;
    }

    @Nonnull
    public FrameChangeGenerator create(FrameUpdate frameUpdate) {
        return new FrameChangeGenerator(checkNotNull(frameUpdate),
                                        projectOntologiesIndex,
                                        reverseEngineeredChangeDescriptionGeneratorFactory,
                                        defaultOntologyIdManager,
                                        ontologyAxiomsIndex,
                                        objectPropertyFrameTranslator,
                                        dataPropertyFrameTranslator,
                                        annotationPropertyFrameTranslator,
                                        namedIndividualFrameTranslator,
                                        renderingManager,
                                        classFrameProvider,
                                        classFrame2FrameAxiomsTranslator);
    }
}
