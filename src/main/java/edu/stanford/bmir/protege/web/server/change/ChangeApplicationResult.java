package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class ChangeApplicationResult<S> implements HasSubject<Optional<S>> {

    private RenameMap renameMap;

    private List<OWLOntologyChange> changeList;

    private Optional<S> subject;

    public ChangeApplicationResult(Optional<S> subject, List<OWLOntologyChange> changeList, RenameMap renameMap) {
        this.subject = subject;
        this.changeList = new ArrayList<OWLOntologyChange>(changeList);
        this.renameMap = renameMap;
    }

    public RenameMap getRenameMap() {
        return renameMap;
    }

    public List<OWLOntologyChange> getChangeList() {
        return Collections.unmodifiableList(changeList);
    }

    public <E extends OWLEntity> E getRenamedEntity(E entity) {
        return renameMap.getRenamedEntity(entity);
    }

    @Override
    public Optional<S> getSubject() {
        return subject;
    }
}
