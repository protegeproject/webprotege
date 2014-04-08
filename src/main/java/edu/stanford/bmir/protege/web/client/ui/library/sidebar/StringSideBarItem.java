package edu.stanford.bmir.protege.web.client.ui.library.sidebar;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/01/2012
 */
public class StringSideBarItem implements SideBarItem {

    private String label;

    public StringSideBarItem(String label) {
        this.label = label;
    }

    /**
     * Gets the label of this thing.
     * @return A string representing the label
     */
    public String getLabel() {
        return label;
    }
}
