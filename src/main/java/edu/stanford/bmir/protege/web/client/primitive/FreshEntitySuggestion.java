package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 *
 * A suggestion that suggests a fresh entity is created.  New axioms may also be suggested as part of this suggestion.
 */
public class FreshEntitySuggestion extends EntitySuggestion {

    private List<OWLAxiom> augmentingAxioms;

    public FreshEntitySuggestion(OWLEntityData entity, String displayText, Set<OWLAxiom> augmentingAxioms) {
        super(entity, displayText);
        this.augmentingAxioms = Lists.newArrayList(augmentingAxioms);
    }

    public Collection<OWLAxiom> getAugmentingAxioms() {
        return Lists.newArrayList(augmentingAxioms);
    }
}
