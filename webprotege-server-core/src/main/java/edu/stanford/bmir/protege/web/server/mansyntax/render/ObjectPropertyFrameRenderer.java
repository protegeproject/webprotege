package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyFrameRenderer implements FrameRenderer<OWLObjectProperty> {

    @Nonnull
    private final AnnotationsSectionRenderer<OWLObjectProperty> annotationsSectionRenderer;

    @Nonnull
    private final ObjectPropertyDomainSectionRenderer domainSectionRenderer;

    @Nonnull
    private final ObjectPropertyRangeSectionRenderer rangeSectionRenderer;

    @Nonnull
    private final ObjectPropertyCharacteristicsSectionRenderer characteristicsSectionRenderer;

    @Nonnull
    private final ObjectPropertySubPropertyOfRenderer subPropertyOfRenderer;

    @Nonnull
    private final ObjectPropertyEquivalentToSectionRenderer equivalentToSectionRenderer;

    @Nonnull
    private final ObjectPropertyDisjointWithSectionRenderer disjointWithSectionRenderer;

    @Nonnull
    private final ObjectPropertyInverseOfSectionRenderer inverseOfSectionRenderer;

    @Inject
    public ObjectPropertyFrameRenderer(@Nonnull AnnotationsSectionRenderer<OWLObjectProperty> annotationsSectionRenderer,
                                       @Nonnull ObjectPropertyDomainSectionRenderer domainSectionRenderer,
                                       @Nonnull ObjectPropertyRangeSectionRenderer rangeSectionRenderer,
                                       @Nonnull ObjectPropertyCharacteristicsSectionRenderer characteristicsSectionRenderer,
                                       @Nonnull ObjectPropertySubPropertyOfRenderer subPropertyOfRenderer,
                                       @Nonnull ObjectPropertyEquivalentToSectionRenderer equivalentToSectionRenderer,
                                       @Nonnull ObjectPropertyDisjointWithSectionRenderer disjointWithSectionRenderer,
                                       @Nonnull ObjectPropertyInverseOfSectionRenderer inverseOfSectionRenderer,
                                       @Nonnull ObjectPropertySubPropertyChainSectionRenderer subPropertyChainSectionRenderer) {
        this.annotationsSectionRenderer = annotationsSectionRenderer;
        this.domainSectionRenderer = domainSectionRenderer;
        this.rangeSectionRenderer = rangeSectionRenderer;
        this.characteristicsSectionRenderer = characteristicsSectionRenderer;
        this.subPropertyOfRenderer = subPropertyOfRenderer;
        this.equivalentToSectionRenderer = equivalentToSectionRenderer;
        this.disjointWithSectionRenderer = disjointWithSectionRenderer;
        this.inverseOfSectionRenderer = inverseOfSectionRenderer;
        this.subPropertyChainSectionRenderer = subPropertyChainSectionRenderer;
    }

    @Nonnull
    private final ObjectPropertySubPropertyChainSectionRenderer subPropertyChainSectionRenderer;

    @Override
    public List<FrameSectionRenderer<OWLObjectProperty, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLObjectProperty, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(annotationsSectionRenderer);
        renderers.add(domainSectionRenderer);
        renderers.add(rangeSectionRenderer);
        renderers.add(characteristicsSectionRenderer);
        renderers.add(subPropertyOfRenderer);
        renderers.add(equivalentToSectionRenderer);
        renderers.add(disjointWithSectionRenderer);
        renderers.add(inverseOfSectionRenderer);
        renderers.add(subPropertyChainSectionRenderer);
        return renderers;
    }
}
