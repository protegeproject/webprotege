package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormView extends IsWidget {

    void addFormElementView(FormFieldView view,
                            FieldRun fieldRun);

    List<FormFieldView> getFieldViews();

    void clear();

    void requestFocus();
}
