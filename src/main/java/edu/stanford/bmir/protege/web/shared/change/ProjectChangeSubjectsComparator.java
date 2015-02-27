package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class ProjectChangeSubjectsComparator implements Comparator<ProjectChange> {

    public static final int BEFORE = -1;
    public static final int AFTER = 1;
    public static final int SAME = 0;

    @Override
    public int compare(ProjectChange o1, ProjectChange o2) {
        ImmutableSet<OWLEntityData> subjects1 = o1.getSubjects();
        ImmutableSet<OWLEntityData> subjects2 = o2.getSubjects();
        if(subjects1.isEmpty()) {
            if(subjects2.isEmpty()) {
                return SAME;
            }
            else {
                return AFTER;
            }
        }
        else {
            if(subjects2.isEmpty()) {
                return BEFORE;
            }
            else {
                return Ordering.from(new Comparator<OWLEntityData>() {
                    @Override
                    public int compare(OWLEntityData o1, OWLEntityData o2) {
                        return o1.getBrowserText().compareToIgnoreCase(o2.getBrowserText());
                    }
                }).lexicographical().compare(subjects1, subjects2);
            }
        }
    }
}
