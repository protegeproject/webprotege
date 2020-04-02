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
    private final ReverseEngineeredChangeDescriptionGeneratorFactory factory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final OntologyAxiomsIndex axiomsIndex;

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

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public FrameChangeGeneratorFactory(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                       @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory factory,
                                       @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                       @Nonnull OntologyAxiomsIndex axiomsIndex,
                                       @Nonnull ClassFrameTranslator classFrameTranslator,
                                       @Nonnull ObjectPropertyFrameTranslator objectPropertyFrameTranslator,
                                       @Nonnull DataPropertyFrameTranslator dataPropertyFrameTranslator,
                                       @Nonnull AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator,
                                       @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator,
                                       @Nonnull RenderingManager renderingManager) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.factory = factory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.axiomsIndex = axiomsIndex;
        this.classFrameTranslator = classFrameTranslator;
        this.objectPropertyFrameTranslator = objectPropertyFrameTranslator;
        this.dataPropertyFrameTranslator = dataPropertyFrameTranslator;
        this.annotationPropertyFrameTranslator = annotationPropertyFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    public FrameChangeGenerator create(FrameUpdate frameUpdate) {
        return new FrameChangeGenerator(checkNotNull(frameUpdate),
                                        projectOntologiesIndex,
                                        factory,
                                        this.defaultOntologyIdManager,
                                        this.axiomsIndex,
                                        this.classFrameTranslator,
                                        this.objectPropertyFrameTranslator,
                                        this.dataPropertyFrameTranslator,
                                        this.annotationPropertyFrameTranslator,
                                        this.namedIndividualFrameTranslator,
                                        renderingManager);
    }
}
