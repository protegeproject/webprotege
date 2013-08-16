package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UpdateEntityChangeListGenerator<E extends OWLEntity> implements ChangeListGenerator<E> {

    @Override
    public OntologyChangeList<E> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();

        return builder.build();
    }

    @Override
    public E getRenamedResult(E result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
