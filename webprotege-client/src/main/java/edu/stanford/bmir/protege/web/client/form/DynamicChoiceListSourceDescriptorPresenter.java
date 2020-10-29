package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class DynamicChoiceListSourceDescriptorPresenter {

    @Nonnull
    private final DynamicChoiceListSourceDescriptorView view;

    @Nonnull
    private final EntityCriteriaPresenter criteriaPresenter;

    @Inject
    public DynamicChoiceListSourceDescriptorPresenter(@Nonnull DynamicChoiceListSourceDescriptorView view,
                                                      @Nonnull EntityCriteriaPresenter criteriaPresenter) {
        this.view = checkNotNull(view);
        this.criteriaPresenter = checkNotNull(criteriaPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        criteriaPresenter.start(view.getCriteriaContainer());
    }

    public void setDescriptor(@Nonnull DynamicChoiceListSourceDescriptor descriptor) {
        criteriaPresenter.setCriteria(descriptor.getCriteria().asCompositeRootCriteria());
    }

    public DynamicChoiceListSourceDescriptor getDescriptor() {
        return criteriaPresenter.getCriteria()
                                .map(DynamicChoiceListSourceDescriptor::get)
                                .orElse(DynamicChoiceListSourceDescriptor.get(EntityIsCriteria.get(DataFactory.getOWLThing())));
    }
}
