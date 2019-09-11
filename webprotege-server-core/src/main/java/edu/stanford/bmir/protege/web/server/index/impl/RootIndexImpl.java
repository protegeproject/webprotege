package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.RootIndex;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-10
 */
public class RootIndexImpl implements RootIndex {

    @Nonnull
    private final OntologyAxiomsIndex ontologyAxiomsIndex;

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final ChangeFilter changeFilter = new ChangeFilter();

    @Inject
    public RootIndexImpl(@Nonnull OntologyAxiomsIndex ontologyAxiomsIndex,
                         @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex) {
        this.ontologyAxiomsIndex = ontologyAxiomsIndex;
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
    }

    @Nonnull
    @Override
    public List<OntologyChange> filterEffectiveChanges(@Nonnull List<OntologyChange> changes) {
        var resultBuilder = ImmutableList.<OntologyChange>builder();
        for(var chg : changes) {
            if(chg.accept(changeFilter).equals(Boolean.TRUE)) {
                resultBuilder.add(chg);
            }
        }
        return resultBuilder.build();
    }

    private class ChangeFilter implements OntologyChangeVisitorEx<Boolean> {

        @Override
        public Boolean getDefaultReturnValue() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean visit(@Nonnull AddAxiomChange addAxiomChange) {
            var axiom = addAxiomChange.getAxiom();
            var ontologyId = addAxiomChange.getOntologyId();
            if(!ontologyAxiomsIndex.containsAxiom(axiom, ontologyId)) {
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
        }

        @Override
        public Boolean visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
            var axiom = removeAxiomChange.getAxiom();
            var ontologyId = removeAxiomChange.getOntologyId();
            if(ontologyAxiomsIndex.containsAxiom(axiom, ontologyId)) {
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
        }

        @Override
        public Boolean visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {
            var annotation = addOntologyAnnotationChange.getAnnotation();
            var ontologyId = addOntologyAnnotationChange.getOntologyId();
            if(!ontologyAnnotationsIndex.containsAnnotation(annotation, ontologyId)) {
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
        }

        @Override
        public Boolean visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
            var annotation = removeOntologyAnnotationChange.getAnnotation();
            var ontologyId = removeOntologyAnnotationChange.getOntologyId();
            if(ontologyAnnotationsIndex.containsAnnotation(annotation, ontologyId)) {
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
        }
    }
}
