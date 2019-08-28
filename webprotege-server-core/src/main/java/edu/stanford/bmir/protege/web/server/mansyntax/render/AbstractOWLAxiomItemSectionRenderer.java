package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public abstract class AbstractOWLAxiomItemSectionRenderer<E extends OWLObject, I extends OWLAxiom, R> extends AbstractSectionRenderer<E, I, R> {


    @Override
    public final Formatting getSectionFormatting() {
        return Formatting.LINE_PER_ITEM;
    }

    @Override
    public final List<I> getItemsInOntology(E subject, OWLOntologyID ontologyId, ShortFormProvider shortFormProvider, Comparator<OWLObject> comparator) {
        return sort(getAxiomsInOntology(subject, ontologyId), shortFormProvider);
    }

    protected abstract Set<I> getAxiomsInOntology(E subject, OWLOntologyID ontologyId);

    @Override
    public List<OWLAnnotation> getAnnotations(I item) {
        return Lists.newArrayList(item.getAnnotations());
    }


    protected List<I> sort(Set<I> items, ShortFormProvider shortFormProvider) {
        ArrayList<I> result = Lists.newArrayList(items);
        Collections.sort(result);
        return result;
    }

}
