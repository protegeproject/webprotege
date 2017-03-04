package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyFrameRenderer implements FrameRenderer<OWLAnnotationProperty> {

    @Override
    public List<FrameSectionRenderer<OWLAnnotationProperty, ?, ?>> getSectionRenderers() {
        return Lists.<FrameSectionRenderer<OWLAnnotationProperty, ?, ?>>newArrayList(
                new AnnotationsSectionRenderer<OWLAnnotationProperty>(),
                new AnnotationPropertySubPropertyOfSectionRenderer(),
                new AnnotationPropertyDomainSectionRenderer(),
                new AnnotationPropertyRangeSectionRenderer()
        );
    }
}
