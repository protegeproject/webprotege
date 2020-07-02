package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.RootCriteriaPresenter;
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
    private final RootCriteriaPresenter rootCriteriaPresenter;

    @Inject
    public DynamicChoiceListSourceDescriptorPresenter(@Nonnull DynamicChoiceListSourceDescriptorView view,
                                                      @Nonnull RootCriteriaPresenter rootCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.rootCriteriaPresenter = checkNotNull(rootCriteriaPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        rootCriteriaPresenter.start(view.getCriteriaContainer());
    }

    public void setDescriptor(@Nonnull DynamicChoiceListSourceDescriptor descriptor) {
        rootCriteriaPresenter.setCriteria(descriptor.getCriteria());
    }

    public DynamicChoiceListSourceDescriptor getDescriptor() {
        return rootCriteriaPresenter.getCriteria()
                             .map(DynamicChoiceListSourceDescriptor::get)
                             .orElse(DynamicChoiceListSourceDescriptor.get(EntityIsCriteria.get(DataFactory.getOWLThing())));
    }
}
