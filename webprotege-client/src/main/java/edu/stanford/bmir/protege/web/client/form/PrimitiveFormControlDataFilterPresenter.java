package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public interface PrimitiveFormControlDataFilterPresenter {

    void start(@Nonnull AcceptsOneWidget container);

    void setFilterValueChangedHandler(@Nonnull PrimitiveDataFormControlValueFilterChangedHandler handler);

    @Nonnull
    Optional<PrimitiveFormControlDataMatchCriteria> getFilterCriteria();
}
