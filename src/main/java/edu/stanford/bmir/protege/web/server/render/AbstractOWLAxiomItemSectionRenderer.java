package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
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
    public final List<I> getItemsInOntology(E subject, OWLOntology ontology, ShortFormProvider shortFormProvider, Comparator<OWLObject> comparator) {
        return sort(getAxiomsInOntology(subject, ontology), shortFormProvider);
    }

    protected abstract Set<I> getAxiomsInOntology(E subject, OWLOntology ontology);

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
