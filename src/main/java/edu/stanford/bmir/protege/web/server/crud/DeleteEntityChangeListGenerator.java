package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class DeleteEntityChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    private OWLEntity entity;

    public DeleteEntityChangeListGenerator(OWLEntity entity) {
        this.entity = entity;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(Project project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<OWLEntity> builder = new OntologyChangeList.Builder<OWLEntity>();
        OWLEntityRemover remover = new OWLEntityRemover(
                project.getRootOntology().getImportsClosure());
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
