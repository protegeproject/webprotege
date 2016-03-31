package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormView extends IsWidget {

    void addFormElementView(FormElementView view);

    List<FormElementView> getElementViews();

    void clear();

}
