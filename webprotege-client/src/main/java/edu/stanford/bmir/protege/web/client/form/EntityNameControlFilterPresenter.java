package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptorDto;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-17
 */
public class EntityNameControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final EntityNameControlFilterView view;

    @Nonnull
    private final EntityNameControlDescriptorDto descriptor;

    @AutoFactory
    public EntityNameControlFilterPresenter(@Provided @Nonnull EntityNameControlFilterView view,
                                            @Provided @Nonnull EntityCriteriaPresenter entityCriteriaPresenter,
                                            @Nonnull EntityNameControlDescriptorDto descriptor) {
        this.view = view;
        this.descriptor = descriptor;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        descriptor.getMatchCriteria()
                  .ifPresent(view::setEntityMatchCriteria);
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return Optional.empty();
    }
}
