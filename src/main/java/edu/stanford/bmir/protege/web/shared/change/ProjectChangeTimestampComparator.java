package edu.stanford.bmir.protege.web.shared.change;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class ProjectChangeTimestampComparator implements Comparator<ProjectChange> {

    @Override
    public int compare(ProjectChange o1, ProjectChange o2) {
        return (int) (o1.getTimestamp() - o2.getTimestamp());
    }
}
