package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class AbstractCreateEntityAction<R extends AbstractCreateEntityResult<E>, E extends OWLEntity> extends AbstractHasProjectAction<R> {

    private Set<String> browserTexts;


    public AbstractCreateEntityAction(ProjectId projectId, Set<String> browserTexts) {
        super(projectId);
        this.browserTexts = new HashSet<String>(browserTexts);
    }

    protected AbstractCreateEntityAction() {
    }

    public Set<String> getBrowserTexts() {
        return Collections.unmodifiableSet(browserTexts);
    }


}
