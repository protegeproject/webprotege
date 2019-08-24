package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyFrameRenderer implements FrameRenderer<OWLAnnotationProperty> {

    @Nonnull
    private final AnnotationsSectionRenderer<OWLAnnotationProperty> annotationsSectionRenderer;

    @Nonnull
    private final AnnotationPropertyDomainSectionRenderer domainSectionRenderer;

    @Nonnull
    private final AnnotationPropertyRangeSectionRenderer rangeSectionRenderer;

    @Nonnull
    private final AnnotationPropertySubPropertyOfSectionRenderer subPropertyOfSectionRenderer;

    @Inject
    public AnnotationPropertyFrameRenderer(@Nonnull AnnotationsSectionRenderer<OWLAnnotationProperty> annotationsSectionRenderer,
                                           @Nonnull AnnotationPropertyDomainSectionRenderer domainSectionRenderer,
                                           @Nonnull AnnotationPropertyRangeSectionRenderer rangeSectionRenderer,
                                           @Nonnull AnnotationPropertySubPropertyOfSectionRenderer subPropertyOfSectionRenderer) {
        this.annotationsSectionRenderer = annotationsSectionRenderer;
        this.domainSectionRenderer = domainSectionRenderer;
        this.rangeSectionRenderer = rangeSectionRenderer;
        this.subPropertyOfSectionRenderer = subPropertyOfSectionRenderer;
    }

    @Override
    public List<FrameSectionRenderer<OWLAnnotationProperty, ?, ?>> getSectionRenderers() {
        return Lists.newArrayList(
                annotationsSectionRenderer,
                subPropertyOfSectionRenderer,
                domainSectionRenderer,
                rangeSectionRenderer
        );
    }
}
