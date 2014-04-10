package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

import java.util.Comparator;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class AnnotationsGroupComparator implements Comparator<AnnotationsGroup> {

    private ShortFormProvider shortFormProvider;

    private ManchesterOWLSyntaxOWLObjectRendererImpl impl;

    private AnnotationPropertyComparator annotationPropertyComparator;

    public AnnotationsGroupComparator(AnnotationPropertyComparator annotationPropertyComparator, ShortFormProvider shortFormProvider) {
        this.annotationPropertyComparator = annotationPropertyComparator;
        this.shortFormProvider = shortFormProvider;
        impl = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        impl.setShortFormProvider(shortFormProvider);
    }

    @Override
    public int compare(AnnotationsGroup annotationsGroup, AnnotationsGroup annotationsGroup2) {


        int propertyDiff =  annotationPropertyComparator.compareTo(annotationsGroup.getProperty(),
                annotationsGroup2.getProperty(),
                shortFormProvider);
        if(propertyDiff != 0) {
            return propertyDiff;
        }
        OWLObject owlObject1 = annotationsGroup.getRenderables().get(1);
        OWLObject owlObject2 = annotationsGroup.getRenderables().get(1);
        String ren1 = impl.render(owlObject1);
        String ren2 = impl.render(owlObject2);
        return ren1.compareTo(ren2);
    }
}
