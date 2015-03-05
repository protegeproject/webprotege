package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class IndirectlyWatchedEntitiesFinder {

    private final OWLOntology rootOntology;

    private final edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider<OWLClass> classHierarchyProvider;

    private final OWLObjectHierarchyProvider<OWLObjectProperty> objectPropertyHierarchyProvider;

    private final OWLObjectHierarchyProvider<OWLDataProperty> dataPropertyHierarchyProvider;


    @Inject
    public IndirectlyWatchedEntitiesFinder(@RootOntology OWLOntology rootOntology,
                                           OWLObjectHierarchyProvider<OWLClass> classHierarchyProvider,
                                           OWLObjectHierarchyProvider<OWLObjectProperty> objectPropertyHierarchyProvider,
                                           OWLObjectHierarchyProvider<OWLDataProperty> dataPropertyHierarchyProvider) {
        this.rootOntology = checkNotNull(rootOntology);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.objectPropertyHierarchyProvider = checkNotNull(objectPropertyHierarchyProvider);
        this.dataPropertyHierarchyProvider = checkNotNull(dataPropertyHierarchyProvider);
    }

    public Set<? extends OWLEntity> getRelatedWatchedEntities(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorExAdapter<Set<? extends OWLEntity>>() {
            @Override
            protected Set<? extends OWLEntity> getDefaultReturnValue(OWLEntity object) {
                return Collections.emptySet();
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLClass desc) {
                return classHierarchyProvider.getAncestors(desc);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLDataProperty property) {
                return dataPropertyHierarchyProvider.getAncestors(property);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLObjectProperty property) {
                return objectPropertyHierarchyProvider.getAncestors(property);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLNamedIndividual individual) {
                Set<OWLClassExpression> types = individual.getTypes(rootOntology.getImportsClosure());
                Set<OWLClass> result = new HashSet<OWLClass>();
                for(OWLClassExpression ce : types) {
                    if(!ce.isAnonymous()) {
                        result.addAll(classHierarchyProvider.getAncestors(ce.asOWLClass()));
                    }
                }
                return result;
            }
        });
    }

}
