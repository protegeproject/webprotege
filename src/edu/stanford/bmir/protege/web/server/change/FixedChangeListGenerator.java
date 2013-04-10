package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class FixedChangeListGenerator implements ChangeListGenerator<Object> {

    private List<OWLOntologyChange> fixedChangeList;

    public FixedChangeListGenerator(List<OWLOntologyChange> fixedChangeList) {
        this.fixedChangeList = new ArrayList<OWLOntologyChange>(fixedChangeList);
    }

    @Override
    public GeneratedOntologyChanges<Object> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        GeneratedOntologyChanges.Builder<Object> builder = new GeneratedOntologyChanges.Builder<Object>();
        builder.addAll(fixedChangeList);
        return builder.build();
    }

    @Override
    public Object getRenamedResult(Object result, RenameMap renameMap) {
        return result;
    }
}
