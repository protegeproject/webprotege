package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ClassFrameRenderer implements FrameRenderer<OWLClass> {

    @Nonnull
    private final AnnotationsSectionRenderer<OWLClass> annotationsSectionRenderer;

    @Nonnull
    private final ClassSubClassOfSectionRenderer subClassOfSectionRenderer;

    @Nonnull
    private final ClassEquivalentToSectionRenderer equivalentToSectionRenderer;

    @Nonnull
    private final ClassDisjointWithSectionRenderer disjointWithSectionRenderer;

    @Inject
    public ClassFrameRenderer(@Nonnull AnnotationsSectionRenderer<OWLClass> annotationsSectionRenderer,
                              @Nonnull ClassSubClassOfSectionRenderer subClassOfSectionRenderer,
                              @Nonnull ClassEquivalentToSectionRenderer equivalentToSectionRenderer,
                              @Nonnull ClassDisjointWithSectionRenderer disjointWithSectionRenderer) {
        this.annotationsSectionRenderer = annotationsSectionRenderer;
        this.subClassOfSectionRenderer = subClassOfSectionRenderer;
        this.equivalentToSectionRenderer = equivalentToSectionRenderer;
        this.disjointWithSectionRenderer = disjointWithSectionRenderer;
    }

    @Override
    public List<FrameSectionRenderer<OWLClass, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLClass, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(annotationsSectionRenderer);
        renderers.add(subClassOfSectionRenderer);
        renderers.add(equivalentToSectionRenderer);
        renderers.add(disjointWithSectionRenderer);
        return renderers;

    }
}
