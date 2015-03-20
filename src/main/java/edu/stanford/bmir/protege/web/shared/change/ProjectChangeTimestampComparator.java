package edu.stanford.bmir.protege.web.shared.change;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class ProjectChangeTimestampComparator implements Comparator<ProjectChange> {

    private static final int ONE_BEFORE_TWO = -1;

    private static final int SAME = 0;

    private static final int ONE_AFTER_TWO = 1;

    @Override
    public int compare(ProjectChange o1, ProjectChange o2) {
        long timestamp1 = o1.getTimestamp();
        long timestamp2 = o2.getTimestamp();
        if(timestamp1 < timestamp2) {
            return ONE_BEFORE_TWO;
        }
        else if(timestamp1 == timestamp2) {
            return SAME;
        }
        else {
            return ONE_AFTER_TWO;
        }
    }
}
