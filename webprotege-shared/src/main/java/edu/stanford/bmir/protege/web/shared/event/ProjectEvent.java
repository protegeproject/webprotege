package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public abstract class ProjectEvent<H> extends WebProtegeEvent<H> implements HasProjectId {

    protected ProjectEvent(ProjectId source) {
        setSource(source);
    }

    protected ProjectEvent() {
    }


    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return getSource();
    }
}
