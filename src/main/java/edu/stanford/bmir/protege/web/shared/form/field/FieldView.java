package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FieldView extends IsWidget, HasEnabled {

    void setValue(OWLPrimitiveData value);

    void clearValue();

    Optional<OWLPrimitiveData> getValue();
}
