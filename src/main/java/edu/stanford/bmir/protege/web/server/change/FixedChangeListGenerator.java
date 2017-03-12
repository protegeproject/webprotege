package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class FixedChangeListGenerator<R> implements ChangeListGenerator<R> {

    private List<OWLOntologyChange> fixedChangeList;

    public FixedChangeListGenerator(List<OWLOntologyChange> fixedChangeList) {
        this.fixedChangeList = new ArrayList<OWLOntologyChange>(fixedChangeList);
    }

    public static <S> FixedChangeListGenerator<S> get(List<OWLOntologyChange> changes) {
        return new FixedChangeListGenerator<S>(changes);
    }

    @Override
    public OntologyChangeList<R> generateChanges(Project project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<R> builder = new OntologyChangeList.Builder<R>();
        builder.addAll(fixedChangeList);
        return builder.build();
    }

    @Override
    public R getRenamedResult(R result, RenameMap renameMap) {
        return result;
    }
}
