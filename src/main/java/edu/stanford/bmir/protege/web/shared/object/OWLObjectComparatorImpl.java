package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class OWLObjectComparatorImpl implements Comparator<OWLObject> {

    private OWLObjectRenderer renderer;

    @Inject
    public OWLObjectComparatorImpl(OWLObjectRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public int compare(OWLObject owlObject, OWLObject owlObject2) {
        String rendering = renderer.render(owlObject);
        String rendering2 = renderer.render(owlObject2);
        return rendering.compareToIgnoreCase(rendering2);
    }
}
