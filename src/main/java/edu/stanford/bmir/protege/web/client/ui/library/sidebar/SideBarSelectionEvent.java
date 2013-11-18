package edu.stanford.bmir.protege.web.client.ui.library.sidebar;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/01/2012
 */
public class SideBarSelectionEvent {

    private SideBar sideBar;

    public SideBarSelectionEvent(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    public SideBar getSideBar() {
        return sideBar;
    }
}
