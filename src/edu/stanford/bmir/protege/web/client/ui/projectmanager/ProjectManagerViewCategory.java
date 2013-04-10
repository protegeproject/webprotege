package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.client.ui.library.common.HasLabel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectManagerViewCategory implements HasLabel {


    public static final ProjectManagerViewCategory HOME = new ProjectManagerViewCategory("Home");

    public static final ProjectManagerViewCategory OWNED_BY_ME = new ProjectManagerViewCategory("Owned By Me");

    public static final ProjectManagerViewCategory TRASH = new ProjectManagerViewCategory("Trash");

    private String label;

    public ProjectManagerViewCategory(String label) {
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
        if(!(obj instanceof ProjectManagerViewCategory)) {
            return false;
        }
        ProjectManagerViewCategory other = (ProjectManagerViewCategory) obj;
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
