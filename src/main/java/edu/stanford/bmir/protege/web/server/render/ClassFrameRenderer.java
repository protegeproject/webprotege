package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ClassFrameRenderer implements FrameRenderer<OWLClass> {

    @Override
    public List<FrameSectionRenderer<OWLClass, ?, ?>> getSectionRenderers() {
        List<FrameSectionRenderer<OWLClass, ?, ?>> renderers = Lists.newArrayList();
        renderers.add(new AnnotationsSectionRenderer<OWLClass>());
        renderers.add(new ClassSubClassOfSectionRenderer());
        renderers.add(new ClassEquivalentToSectionRenderer());
        renderers.add(new ClassDisjointWithSectionRenderer());
//        renderers.add(new ClassDomainOfSectionRenderer());
//        renderers.add(new ClassRangeOfSectionRenderer());
        return renderers;

    }
}
