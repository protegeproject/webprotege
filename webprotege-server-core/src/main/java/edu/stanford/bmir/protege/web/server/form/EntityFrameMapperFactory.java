package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-26
 */
public class EntityFrameMapperFactory {

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex classAssertionAxiomsIndex;

    @Nonnull
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private RenderingManager renderingManager;

    @Inject
    public EntityFrameMapperFactory(@Nonnull ClassAssertionAxiomsByClassIndex classAssertionAxiomsIndex,
                                    @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull RenderingManager renderingManager) {
        this.classAssertionAxiomsIndex = checkNotNull(classAssertionAxiomsIndex);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.renderingManager = renderingManager;
    }

    public EntityFrameMapper create(@Nonnull EntityFrame<?> entityFrame) {
        return new EntityFrameMapper(entityFrame, projectOntologiesIndex, classAssertionAxiomsIndex, renderingManager);
    }
}
