package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormElementDescriptorListPresenter implements Presenter {


    @Nonnull
    private FormElementDescriptorListView view;

    @Nonnull
    private final Provider<FormElementDescriptorPresenter> elementDescriptorEditorPresenterProvider;

    @Nonnull
    private final Provider<FormElementDescriptorViewHolder> elementDescriptorViewHolderProvider;

    @Nonnull
    private final List<FormElementDescriptorPresenter> descriptorEditorPresenters = new ArrayList<>();

    @Inject
    public FormElementDescriptorListPresenter(@Nonnull FormElementDescriptorListView view,
                                              @Nonnull Provider<FormElementDescriptorPresenter> elementDescriptorEditorPresenterProvider,
                                              @Nonnull Provider<FormElementDescriptorViewHolder> elementDescriptorViewHolderProvider) {
        this.view = checkNotNull(view);
        this.elementDescriptorEditorPresenterProvider = checkNotNull(elementDescriptorEditorPresenterProvider);
        this.elementDescriptorViewHolderProvider = checkNotNull(elementDescriptorViewHolderProvider);
    }

    public void addElement() {
        FormElementDescriptor descriptor = FormElementDescriptor.getDefault();
        addFormElementDescriptor(descriptor);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
    }

    public void clear() {
        view.clear();
        descriptorEditorPresenters.clear();
    }

    public void setDescriptors(@Nonnull List<FormElementDescriptor> descriptors) {
        checkNotNull(descriptors);
        clear();
        descriptors.forEach(this::addFormElementDescriptor);
    }

    public void addFormElementDescriptor(FormElementDescriptor descriptor) {
        FormElementDescriptorPresenter descriptorPresenter = elementDescriptorEditorPresenterProvider.get();
        descriptorEditorPresenters.add(descriptorPresenter);
        descriptorPresenter.setNumber(descriptorEditorPresenters.size());
        FormElementDescriptorViewHolder viewHolder = elementDescriptorViewHolderProvider.get();
        view.addView(viewHolder);
        descriptorPresenter.start(viewHolder);
        descriptorPresenter.setFormElementDescriptor(descriptor);
        descriptorPresenter.setRemoveFormElementDescriptorHandler(() -> {
            descriptorEditorPresenters.remove(descriptorPresenter);
            view.removeView(viewHolder);
            renumberHolders();
        });
    }

    private void renumberHolders() {
        for(int i = 0; i < descriptorEditorPresenters.size(); i++) {
            descriptorEditorPresenters.get(i).setNumber(i + 1);
        }
    }

    @Nonnull
    public List<FormElementDescriptor> getDescriptors() {
        return descriptorEditorPresenters.stream()
                                         .map(p -> p.getFormElementDescriptor())
                                         .filter(Optional::isPresent)
                                         .map(Optional::get)
                                         .collect(toList());
    }
}
