package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.client.library.common.HasLabel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectManagerViewFilter implements HasLabel {


    public static final ProjectManagerViewFilter OWNED_BY_ME = new ProjectManagerViewFilter("Owned by me");

    public static final ProjectManagerViewFilter SHARED_WITH_ME = new ProjectManagerViewFilter("Shared with me");

    public static final ProjectManagerViewFilter TRASH = new ProjectManagerViewFilter("Trash");



    private String label;

    public ProjectManagerViewFilter(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return "ProjectManagerViewCategory".hashCode() + label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectManagerViewFilter)) {
            return false;
        }
        ProjectManagerViewFilter other = (ProjectManagerViewFilter) obj;
        return this.label.equals(other.label);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProjectManagerViewCategory");
        sb.append("(");
        sb.append(label);
        sb.append(")");
        return sb.toString();
    }
}
