package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyFrameRenderer implements FrameRenderer<OWLDataProperty> {

    @Nonnull
    private final AnnotationsSectionRenderer<OWLDataProperty> annotationsSectionRenderer;

    @Nonnull
    private final DataPropertyDomainSectionRenderer domainSectionRenderer;

    @Nonnull
    private final DataPropertyRangeSectionRenderer rangeSectionRenderer;

    @Nonnull
    private final DataPropertyCharacteristicsSectionRenderer characteristicsSectionRenderer;

    @Nonnull
    private final DataPropertySubPropertyOfSectionRenderer subPropertyOfSectionRenderer;

    @Nonnull
    private final DataPropertyEquivalentToSectionRenderer equivalentToSectionRenderer;

    @Nonnull
    private final DataPropertyDisjointWithSectionRenderer disjointWithSectionRenderer;

    @Inject
    public DataPropertyFrameRenderer(@Nonnull AnnotationsSectionRenderer<OWLDataProperty> annotationsSectionRenderer,
                                     @Nonnull DataPropertyDomainSectionRenderer domainSectionRenderer,
                                     @Nonnull DataPropertyRangeSectionRenderer rangeSectionRenderer,
                                     @Nonnull DataPropertyCharacteristicsSectionRenderer characteristicsSectionRenderer,
                                     @Nonnull DataPropertySubPropertyOfSectionRenderer subPropertyOfSectionRenderer,
                                     @Nonnull DataPropertyEquivalentToSectionRenderer equivalentToSectionRenderer,
                                     @Nonnull DataPropertyDisjointWithSectionRenderer disjointWithSectionRenderer) {
        this.annotationsSectionRenderer = annotationsSectionRenderer;
        this.domainSectionRenderer = domainSectionRenderer;
        this.rangeSectionRenderer = rangeSectionRenderer;
        this.characteristicsSectionRenderer = characteristicsSectionRenderer;
        this.subPropertyOfSectionRenderer = subPropertyOfSectionRenderer;
        this.equivalentToSectionRenderer = equivalentToSectionRenderer;
        this.disjointWithSectionRenderer = disjointWithSectionRenderer;
    }

    @Override
    public List<FrameSectionRenderer<OWLDataProperty, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLDataProperty, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(annotationsSectionRenderer);
        renderers.add(domainSectionRenderer);
        renderers.add(rangeSectionRenderer);
        renderers.add(characteristicsSectionRenderer);
        renderers.add(subPropertyOfSectionRenderer);
        renderers.add(equivalentToSectionRenderer);
        renderers.add(disjointWithSectionRenderer);
        return renderers;
    }
}
