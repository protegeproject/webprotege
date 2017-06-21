package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class DeleteEntityChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DeleteEntityChangeListGenerator(@Nonnull OWLEntity entity,
                                           @Nonnull OWLOntology rootOntology) {
        this.entity = entity;
        this.rootOntology = rootOntology;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<OWLEntity> builder = new OntologyChangeList.Builder<OWLEntity>();
        OWLEntityRemover remover = new OWLEntityRemover(
                rootOntology.getImportsClosure());
        entity.accept(remover);
        List<RemoveAxiom> changeList = remover.getChanges();
        builder.addAll(changeList);
        return builder.build();
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
