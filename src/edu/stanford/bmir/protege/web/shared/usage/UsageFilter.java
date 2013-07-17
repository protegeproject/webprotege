package edu.stanford.bmir.protege.web.shared.usage;

import org.semanticweb.owlapi.model.AxiomType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public class UsageFilter implements Serializable {

    private boolean showDefiningAxioms = true;

    private Set<AxiomType<?>> axiomTypes;

    public UsageFilter() {
        axiomTypes = new HashSet<AxiomType<?>>();
    }

    public UsageFilter(boolean showDefiningAxioms, Set<AxiomType<?>> axiomTypes) {
        this.showDefiningAxioms = showDefiningAxioms;
        this.axiomTypes = new HashSet<AxiomType<?>>(axiomTypes);
    }

    public boolean isShowDefiningAxioms() {
        return showDefiningAxioms;
    }

    public boolean isFiltering() {
        return !showDefiningAxioms || !axiomTypes.containsAll(AxiomType.AXIOM_TYPES);
    }

    public boolean isIncluded(AxiomType<?> axiomType) {
        return axiomTypes.isEmpty() || axiomTypes.contains(axiomType);
    }
}
