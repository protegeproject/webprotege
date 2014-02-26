package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualFrameRenderer implements FrameRenderer<OWLNamedIndividual> {

    @Override
    public List<FrameSectionRenderer<OWLNamedIndividual, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLNamedIndividual, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(new NamedIndividualTypesSectionRenderer());
        renderers.add(new AnnotationsSectionRenderer<OWLNamedIndividual>());
        renderers.add(new NamedIndividualFactsSectionRenderer());
        renderers.add(new NamedIndividualSameAsSectionRenderer());
        renderers.add(new NamedIndividualDifferentFromSectionRenderer());
        return renderers;
    }
}
