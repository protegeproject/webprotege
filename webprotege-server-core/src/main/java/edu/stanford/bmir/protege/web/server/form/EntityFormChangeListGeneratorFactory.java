package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.EmptyEntityFrameFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
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
    private final EntityFormDataConverter entityFormDataConverter;

    @Nonnull
    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Nonnull
    private final FormFrameConverter formFrameConverter;

    @Nonnull
    private final EmptyEntityFrameFactory emptyEntityFrameFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public EntityFormChangeListGeneratorFactory(@Nonnull EntityFormDataConverter entityFormDataConverter,
                                                @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                                @Nonnull MessageFormatter messageFormatter,
                                                @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                                @Nonnull FormFrameConverter formFrameConverter,
                                                @Nonnull EmptyEntityFrameFactory emptyEntityFrameFactory,
                                                @Nonnull RenderingManager renderingManager) {
        this.entityFormDataConverter = entityFormDataConverter;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.messageFormatter = messageFormatter;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
        this.formFrameConverter = formFrameConverter;
        this.emptyEntityFrameFactory = emptyEntityFrameFactory;
        this.renderingManager = renderingManager;
    }

    public EntityFormChangeListGenerator create(@Nonnull FormData pristineFormData,
                                                @Nonnull FormData formData) {
        checkNotNull(formData);
        checkNotNull(pristineFormData);
        return new EntityFormChangeListGenerator(pristineFormData,
                                                 formData,
                                                 this.entityFormDataConverter,
                                                 this.reverseEngineeredChangeDescriptionGeneratorFactory,
                                                 this.messageFormatter,
                                                 frameChangeGeneratorFactory,
                                                 formFrameConverter, emptyEntityFrameFactory, renderingManager);
    }
}
