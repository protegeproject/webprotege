package edu.stanford.bmir.protege.web.shared.usage;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;

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

    private Set<EntityType<?>> entityTypes;

    public UsageFilter() {
        axiomTypes = new HashSet<AxiomType<?>>();
    }

    public UsageFilter(boolean showDefiningAxioms, Set<EntityType<?>> entityTypes, Set<AxiomType<?>> axiomTypes) {
        this.showDefiningAxioms = showDefiningAxioms;
        this.axiomTypes = new HashSet<AxiomType<?>>(axiomTypes);
        this.entityTypes = new HashSet<EntityType<?>>(entityTypes);
    }

    public boolean isShowDefiningAxioms() {
        return showDefiningAxioms;
    }

    public boolean isFiltering() {
        return !showDefiningAxioms || !axiomTypes.containsAll(AxiomType.AXIOM_TYPES) || !entityTypes.containsAll(EntityType.values());
    }

    public boolean isIncluded(AxiomType<?> axiomType) {
        return axiomTypes.isEmpty() || axiomTypes.contains(axiomType);
    }

    public boolean isIncluded(EntityType<?> entityType) {
        return entityTypes.isEmpty() || entityTypes.contains(entityType);
    }

}
