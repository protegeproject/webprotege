package edu.stanford.bmir.protege.web.shared.events;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/03/2013
 */
public class ProjectEventList extends EventList<ProjectEvent<?>> {

    private ProjectId projectId;

    private ProjectEventList(EventTag startTag, EventTag endTag, ProjectId projectId) {
        super(startTag, endTag);
        this.projectId = projectId;
    }

    private ProjectEventList(EventTag startTag, Collection<ProjectEvent<?>> events, EventTag endTag, ProjectId projectId) {
        super(startTag, events, endTag);
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public static class Builder {

        private ProjectId projectId;

        private EventTag startTag;

        private EventTag endTag;

        private List<ProjectEvent<?>> events = new ArrayList<ProjectEvent<?>>();

        public Builder(EventTag startTag, ProjectId projectId, EventTag endTag) {
            this.startTag = startTag;
            this.projectId = projectId;
            this.endTag = endTag;
        }

        public Builder addEvent(ProjectEvent<?> event) {
            if(!event.getSource().equals(projectId)) {
                throw new IllegalArgumentException("event source is not equal to this builder's projectId");
            }
            events.add(event);
            return this;
        }

        public Builder addEvents(List<? extends  ProjectEvent<?>> events) {
            for(ProjectEvent<?> event : events) {
                addEvent(event);
            }
            return this;
        }

        public ProjectEventList build() {
            return new ProjectEventList(startTag, events, endTag, projectId);
        }

    }


    public static Builder builder(EventTag startTag, ProjectId projectId, EventTag endTag) {
        return new Builder(startTag, projectId, endTag);
    }
}
