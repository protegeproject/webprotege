package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class EntityFormChangeListGeneratorFactory {

    @Nonnull
    private final AxiomTemplatesParser axiomTemplatesParser;

    @Nonnull
    private final EntityFormDataConverter entityFormDataConverter;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator individualFrameTranslator;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGenerator;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Inject
    public EntityFormChangeListGeneratorFactory(@Nonnull AxiomTemplatesParser axiomTemplatesParser,
                                                @Nonnull EntityFormDataConverter entityFormDataConverter,
                                                @Nonnull RenderingManager renderingManager,
                                                @Nonnull ClassFrameTranslator classFrameTranslator,
                                                @Nonnull NamedIndividualFrameTranslator individualFrameTranslator,
                                                @Nonnull FrameChangeGeneratorFactory frameChangeGenerator,
                                                @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                                @Nonnull MessageFormatter messageFormatter) {
        this.axiomTemplatesParser = axiomTemplatesParser;
        this.entityFormDataConverter = entityFormDataConverter;
        this.renderingManager = renderingManager;
        this.classFrameTranslator = classFrameTranslator;
        this.individualFrameTranslator = individualFrameTranslator;
        this.frameChangeGenerator = frameChangeGenerator;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.messageFormatter = messageFormatter;
    }

    public EntityFormChangeListGenerator create(@Nonnull FormData formData) {
        checkNotNull(formData);
        return new EntityFormChangeListGenerator(formData,
                                                 this.axiomTemplatesParser,
                                                 this.entityFormDataConverter,
                                                 this.renderingManager,
                                                 this.classFrameTranslator,
                                                 this.individualFrameTranslator,
                                                 this.frameChangeGenerator,
                                                 this.defaultOntologyIdManager,
                                                 this.reverseEngineeredChangeDescriptionGeneratorFactory,
                                                 this.messageFormatter);
    }
}
