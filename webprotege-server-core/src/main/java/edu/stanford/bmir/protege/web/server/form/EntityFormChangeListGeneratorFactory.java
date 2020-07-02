package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.form.processor.FormDataConverter;
import edu.stanford.bmir.protege.web.server.frame.EmptyEntityFrameFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

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
    private final FormDataConverter formDataProcessor;

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

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager rootOntologyProvider;

    @Inject
    public EntityFormChangeListGeneratorFactory(@Nonnull FormDataConverter formDataProcessor,
                                                @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                                @Nonnull MessageFormatter messageFormatter,
                                                @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                                @Nonnull FormFrameConverter formFrameConverter,
                                                @Nonnull EmptyEntityFrameFactory emptyEntityFrameFactory,
                                                @Nonnull RenderingManager renderingManager,
                                                @Nonnull OWLDataFactory dataFactory,
                                                @Nonnull DefaultOntologyIdManager rootOntologyProvider) {
        this.formDataProcessor = formDataProcessor;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.messageFormatter = messageFormatter;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
        this.formFrameConverter = formFrameConverter;
        this.emptyEntityFrameFactory = emptyEntityFrameFactory;
        this.renderingManager = renderingManager;
        this.dataFactory = dataFactory;
        this.rootOntologyProvider = rootOntologyProvider;
    }

    public EntityFormChangeListGenerator create(@Nonnull OWLEntity subject,
                                                @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                                @Nonnull ImmutableMap<FormId, FormData> formsData) {
        checkNotNull(formsData);
        checkNotNull(pristineFormsData);
        return new EntityFormChangeListGenerator(subject,
                                                 pristineFormsData,
                                                 formsData,
                                                 formDataProcessor,
                                                 messageFormatter,
                                                 frameChangeGeneratorFactory,
                                                 formFrameConverter,
                                                 emptyEntityFrameFactory,
                                                 dataFactory,
                                                 rootOntologyProvider);
    }
}
