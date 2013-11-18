package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2013
 */
public interface CreateEntityPresenter {

    Widget getWidget();

    Focusable getInitialFocusable();

    String getBrowserText();



}
