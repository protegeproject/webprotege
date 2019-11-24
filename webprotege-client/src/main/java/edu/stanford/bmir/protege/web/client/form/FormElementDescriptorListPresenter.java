package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.Collections;
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
    private final List<FormElementDescriptorPresenter> descriptorPresenters = new ArrayList<>();

    @Nonnull
    private final List<FormElementDescriptorViewHolder> viewHolders = new ArrayList<>();

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
        descriptorPresenters.clear();
    }

    public void setDescriptors(@Nonnull List<FormElementDescriptor> descriptors) {
        checkNotNull(descriptors);
        clear();
        descriptors.forEach(this::addFormElementDescriptor);
    }

    public void addFormElementDescriptor(FormElementDescriptor descriptor) {
        FormElementDescriptorPresenter descriptorPresenter = elementDescriptorEditorPresenterProvider.get();
        descriptorPresenters.add(descriptorPresenter);
        FormElementDescriptorViewHolder viewHolder = elementDescriptorViewHolderProvider.get();
        viewHolders.add(viewHolder);
        viewHolder.setNumber(descriptorPresenters.size());
        viewHolder.setHeaderLabel(descriptor.getId().getId());
        view.addView(viewHolder);
        descriptorPresenter.start(viewHolder);
        descriptorPresenter.setElementIdChangedHandler((elementId -> {
            viewHolder.setHeaderLabel(elementId.getId());
        }));
        descriptorPresenter.setFormElementDescriptor(descriptor);
        viewHolder.setRemoveHandler(() -> {
            view.performDeleteElementConfirmation(descriptor.getId(), () -> {
                descriptorPresenters.remove(descriptorPresenter);
                viewHolders.remove(viewHolder);
                view.removeView(viewHolder);
                renumberHolders();
            });
        });
        viewHolder.setMoveUpHandler(() -> {
            moveUp(descriptorPresenter);
            view.moveUp(viewHolder);
            renumberHolders();
        });
        viewHolder.setMoveDownHandler(() -> {
            moveDown(descriptorPresenter);
            view.moveDown(viewHolder);
            renumberHolders();
        });

    }

    public void moveUp(FormElementDescriptorPresenter descriptorPresenter) {
        int fromIndex = descriptorPresenters.indexOf(descriptorPresenter);
        int toIndex = fromIndex - 1;
        if(toIndex > -1) {
            Collections.swap(descriptorPresenters, fromIndex, toIndex);
            Collections.swap(viewHolders, fromIndex, toIndex);
        }

    }

    public void moveDown(FormElementDescriptorPresenter descriptorPresenter) {
        int fromIndex = descriptorPresenters.indexOf(descriptorPresenter);
        int toIndex = fromIndex + 1;
        if(toIndex < descriptorPresenters.size() - 1) {
            Collections.swap(descriptorPresenters, fromIndex, toIndex);
            Collections.swap(viewHolders, fromIndex, toIndex);
        }
    }

    private void renumberHolders() {
        for(int i = 0; i < viewHolders.size(); i++) {
            viewHolders.get(i).setNumber(i + 1);
        }
    }

    @Nonnull
    public List<FormElementDescriptor> getDescriptors() {
        return descriptorPresenters.stream()
                                   .map(p -> p.getFormElementDescriptor())
                                   .filter(Optional::isPresent)
                                   .map(Optional::get)
                                   .collect(toList());
    }
}
