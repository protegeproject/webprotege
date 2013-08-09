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
public class FixedChangeListGenerator<O> implements ChangeListGenerator<O> {

    private List<OWLOntologyChange> fixedChangeList;

    public FixedChangeListGenerator(List<OWLOntologyChange> fixedChangeList) {
        this.fixedChangeList = new ArrayList<OWLOntologyChange>(fixedChangeList);
    }

    @Override
    public GeneratedOntologyChanges<O> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        GeneratedOntologyChanges.Builder<O> builder = new GeneratedOntologyChanges.Builder<O>();
        builder.addAll(fixedChangeList);
        return builder.build();
    }

    @Override
    public O getRenamedResult(O result, RenameMap renameMap) {
        return result;
    }
}
