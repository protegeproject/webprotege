package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OntologyMergeAddPatcher {

    @Nonnull
    private final HasApplyChanges changeManager;

    public OntologyMergeAddPatcher(@Nonnull HasApplyChanges changeManager) {
        this.changeManager = changeManager;
    }

    List<OntologyChange> addAxiomsAndAnnotations(Set<OWLAxiom> axioms, Set<OWLAnnotation> annotations, OWLOntologyID ontologyID){
        var changeList = new ArrayList<OntologyChange>();

        for (OWLAxiom axiom: axioms) {
            changeList.add(AddAxiomChange.of(ontologyID,axiom));
        }

        for (OWLAnnotation annotation: annotations){
            changeList.add(AddOntologyAnnotationChange.of(ontologyID, annotation));
        }

        return changeList;
    }

    void applyChanges(final List<OntologyChange> changes,
                      ExecutionContext executionContext) {
        changeManager.applyChanges(executionContext.getUserId(), new ChangeListGenerator<Boolean>() {
            @Override
            public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
                OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
                builder.addAll(changes);
                return builder.build(!changes.isEmpty());
            }

            @Override
            public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
                return true;
            }

            @Nonnull
            @Override
            public String getMessage(ChangeApplicationResult<Boolean> result) {
                return "Merge ontologies into new ontology";
            }
        });

    }
}
