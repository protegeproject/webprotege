package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public interface FormRegionPresenter {

    @Nonnull
    FormRegionId getFormRegionId();
}
