package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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

    @Override
    protected void setSource(Object source) {
        if(!(source instanceof ProjectId)) {
            throw new RuntimeException("Source should be an instance of ProjectId");
        }
        super.setSource(source);
    }

    @Override
    public ProjectId getSource() {
        return (ProjectId) super.getSource();
    }

    @Override
    public ProjectId getProjectId() {
        return getSource();
    }
}
