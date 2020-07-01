package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public interface FormControlFilterPresenter {

    void start(@Nonnull AcceptsOneWidget container);

    @Nonnull
    Optional<PrimitiveFormControlDataMatchCriteria> getFilter();

    void setFilter(@Nonnull FormRegionFilter filter);

    void clear();
}
