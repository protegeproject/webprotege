package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyFrameRenderer implements FrameRenderer<OWLObjectProperty> {

    @Override
    public List<FrameSectionRenderer<OWLObjectProperty, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLObjectProperty, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(new AnnotationsSectionRenderer<OWLObjectProperty>());
        renderers.add(new ObjectPropertyDomainSectionRenderer());
        renderers.add(new ObjectPropertyRangeSectionRenderer());
        renderers.add(new ObjectPropertyCharacteristicsSectionRenderer());
        renderers.add(new ObjectPropertySubPropertyOfRenderer());
        renderers.add(new ObjectPropertyEquivalentToSectionRenderer());
        renderers.add(new ObjectPropertyDisjointWithSectionRenderer());
        renderers.add(new ObjectPropertyInverseOfSectionRenderer());
        renderers.add(new ObjectPropertySubPropertyChainSectionRenderer());
        return renderers;
    }
}
