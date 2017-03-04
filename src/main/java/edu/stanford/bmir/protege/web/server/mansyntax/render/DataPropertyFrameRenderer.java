package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyFrameRenderer implements FrameRenderer<OWLDataProperty> {

    @Override
    public List<FrameSectionRenderer<OWLDataProperty, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLDataProperty, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(new AnnotationsSectionRenderer<OWLDataProperty>());
        renderers.add(new DataPropertyDomainSectionRenderer());
        renderers.add(new DataPropertyRangeSectionRenderer());
        renderers.add(new DataPropertyCharacteristicsSectionRenderer());
        renderers.add(new DataPropertySubPropertyOfSectionRenderer());
        renderers.add(new DataPropertyEquivalentToSectionRenderer());
        renderers.add(new DataPropertyDisjointWithSectionRenderer());
        return renderers;
    }
}
