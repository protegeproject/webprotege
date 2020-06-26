package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface ChoiceDescriptorView extends IsWidget, HasRequestFocus {

    interface ChoiceValueChangedHandler {
        void handleChoiceValueChanged();
    }

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    LanguageMap getLabel();

    void clear();

    void setPrimitiveData(@Nonnull OWLPrimitiveData dataValue);

    Optional<OWLPrimitiveData> getDataValue();

    void setChoiceValueChangedHandler(@Nonnull ChoiceValueChangedHandler handler);
}
