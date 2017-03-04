package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class RenameEntityChangeFactory extends OWLOntologyChangeFactory {

    private String oldName;
    
    private String newName;

    public RenameEntityChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String oldName, String newName) {
        super(project, userId, changeDescription);
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        Set<OWLOntology> importsClosure = getRootOntology().getImportsClosure();
        OWLEntityRenamer renamer = new OWLEntityRenamer(getRootOntologyManager(), importsClosure);
        Map<OWLEntity, IRI> renameMap = new HashMap<OWLEntity, IRI>();
        RenderingManager rm = getRenderingManager();
        for(OWLEntity oldEntity : rm.getEntities(oldName)) {
            IRI newNameIRI = rm.getIRI(newName);
            renameMap.put(oldEntity, newNameIRI);
        }
        List<OWLOntologyChange> changes = renamer.changeIRI(renameMap);
        changeListToFill.addAll(changes);
    }

}
