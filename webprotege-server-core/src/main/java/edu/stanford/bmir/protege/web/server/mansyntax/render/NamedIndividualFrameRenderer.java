package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualFrameRenderer implements FrameRenderer<OWLNamedIndividual> {

    @Nonnull
    private final NamedIndividualTypesSectionRenderer namedIndividualTypesSectionRenderer;

    @Nonnull
    private final AnnotationsSectionRenderer<OWLNamedIndividual> annotationsSectionRenderer;

    @Nonnull
    private final NamedIndividualFactsSectionRenderer factsSectionRenderer;

    @Nonnull
    private final NamedIndividualSameAsSectionRenderer sameAsSectionRenderer;

    @Nonnull
    private final NamedIndividualDifferentFromSectionRenderer differentFromSectionRenderer;

    @Inject
    public NamedIndividualFrameRenderer(@Nonnull NamedIndividualTypesSectionRenderer namedIndividualTypesSectionRenderer,
                                        @Nonnull AnnotationsSectionRenderer<OWLNamedIndividual> annotationsSectionRenderer,
                                        @Nonnull NamedIndividualFactsSectionRenderer factsSectionRenderer,
                                        @Nonnull NamedIndividualSameAsSectionRenderer sameAsSectionRenderer,
                                        @Nonnull NamedIndividualDifferentFromSectionRenderer differentFromSectionRenderer) {
        this.namedIndividualTypesSectionRenderer = namedIndividualTypesSectionRenderer;
        this.annotationsSectionRenderer = annotationsSectionRenderer;
        this.factsSectionRenderer = factsSectionRenderer;
        this.sameAsSectionRenderer = sameAsSectionRenderer;
        this.differentFromSectionRenderer = differentFromSectionRenderer;
    }

    @Override
    public List<FrameSectionRenderer<OWLNamedIndividual, ?, ?>> getSectionRenderers() {
        var renderers = Lists.<FrameSectionRenderer<OWLNamedIndividual, ?, ?>>newArrayList();
        renderers.add(namedIndividualTypesSectionRenderer);
        renderers.add(annotationsSectionRenderer);
        renderers.add(factsSectionRenderer);
        renderers.add(sameAsSectionRenderer);
        renderers.add(differentFromSectionRenderer);
        return renderers;
    }
}
