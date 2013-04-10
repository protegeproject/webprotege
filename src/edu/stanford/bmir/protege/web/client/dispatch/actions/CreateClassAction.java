package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class CreateClassAction extends AbstractHasProjectAction<CreateClassResult> implements HasBrowserText {

    private String browserText;

    private OWLClass superClass;

    /**
     * For serialization only
     */
    private CreateClassAction() {
    }

    /**
     * Constructs an {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} to "create" a class.  This may involve
     * several ontology changes.
     * @param projectId The identifier of the project to which this action should be applied. Not {@code null}.
     * @param browserText The browser text of the new class. Not {@code null}.
     * @param superClass The class which the new class should be made a subclass of.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateClassAction(ProjectId projectId, String browserText, OWLClass superClass) {
        super(checkNotNull(projectId));
        this.browserText = checkNotNull(browserText);
        this.superClass = checkNotNull(superClass);
    }

    /**
     * Gets the browser text for the new class.
     * @return The browser text.  Not {@code null}.
     */
    @Override
    public String getBrowserText() {
        return browserText;
    }

    @Override
    public String getUnquotedBrowserText() {
        return browserText;
    }

    /**
     * Gets the super class of the new class.
     * @return The class representing the super class. Not {@code null}.
     */
    public OWLClass getSuperClass() {
        return superClass;
    }
}
