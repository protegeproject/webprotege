package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class ObjectListPresenter<T> implements Presenter {


    @Nonnull
    private ObjectListView view;

    @Nonnull
    private final Provider<ObjectPresenter<T>> objectListPresenter;

    @Nonnull
    private final Provider<ObjectListViewHolder> objectViewHolderProvider;

    @Nonnull
    private final Provider<T> defaultObjectProvider;

    @Nonnull
    private final List<ObjectPresenter<T>> objectPresenters = new ArrayList<>();

    @Nonnull
    private final List<ObjectListViewHolder> viewHolders = new ArrayList<>();

    private boolean setDefaultStateCollapsed = false;

    @Inject
    public ObjectListPresenter(@Nonnull ObjectListView view,
                               @Nonnull Provider<ObjectPresenter<T>> objectListPresenter,
                               @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                               @Nonnull Provider<T> defaultObjectProvider) {
        this.view = checkNotNull(view);
        this.objectListPresenter = checkNotNull(objectListPresenter);
        this.objectViewHolderProvider = checkNotNull(objectViewHolderProvider);
        this.defaultObjectProvider = defaultObjectProvider;
    }

    public void addElement() {
        T value = defaultObjectProvider.get();
        addValue(value, true);
    }

    public void setDefaultStateCollapsed() {
        this.setDefaultStateCollapsed = true;
    }

    public void setAllCollapsed() {
        viewHolders.forEach(ObjectListViewHolder::setCollapsed);
    }

    public void setAllExpanded() {
        viewHolders.forEach(ObjectListViewHolder::setExpanded);
    }

    public void setAddObjectText(@Nonnull String addObjectText) {
        view.setAddObjectText(addObjectText);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        view.setAddObjectHandler(this::addElement);
    }

    public void clear() {
        view.clear();
        objectPresenters.clear();
        viewHolders.clear();
    }

    public void setValues(@Nonnull List<T> values) {
        checkNotNull(values);
        clear();
        values.forEach(this::addValue);
        if(setDefaultStateCollapsed) {
            setAllCollapsed();
        }
    }

    public void addValue(T value) {
        addValue(value, false);
    }

    public void addValue(T value, boolean focus) {
        ObjectPresenter<T> descriptorPresenter = objectListPresenter.get();
        objectPresenters.add(descriptorPresenter);
        ObjectListViewHolder viewHolder = objectViewHolderProvider.get();
        viewHolders.add(viewHolder);
        viewHolder.setNumber(objectPresenters.size());
        view.addView(viewHolder);
        descriptorPresenter.start(viewHolder);
        descriptorPresenter.setHeaderLabelChangedHandler((viewHolder::setHeaderLabel));
        descriptorPresenter.setValue(value);
        String headerLabel = descriptorPresenter.getHeaderLabel();
        viewHolder.setHeaderLabel(headerLabel);
        viewHolder.setRemoveHandler(() -> {
            view.performDeleteElementConfirmation(descriptorPresenter.getHeaderLabel(), () -> {
                objectPresenters.remove(descriptorPresenter);
                viewHolders.remove(viewHolder);
                view.removeView(viewHolder);
                renumberHolders();
            });
        });
        viewHolder.setMoveUpHandler(() -> {
            moveUp(descriptorPresenter);
            view.moveUp(viewHolder);
        });
        viewHolder.setMoveDownHandler(() -> {
            moveDown(descriptorPresenter);
            view.moveDown(viewHolder);
        });
        if(focus) {
            viewHolder.requestFocus();
        }
    }

    public void moveUp(ObjectPresenter<T> objectPresenter) {
        int fromIndex = objectPresenters.indexOf(objectPresenter);
        int toIndex = fromIndex - 1;
        if(toIndex > -1) {
            Collections.swap(objectPresenters, fromIndex, toIndex);
            Collections.swap(viewHolders, fromIndex, toIndex);
        }
        renumberHolders();

    }

    public void moveDown(ObjectPresenter<T> objectPresenter) {
        int fromIndex = objectPresenters.indexOf(objectPresenter);
        int toIndex = fromIndex + 1;
        if(toIndex < objectPresenters.size()) {
            Collections.swap(objectPresenters, fromIndex, toIndex);
            Collections.swap(viewHolders, fromIndex, toIndex);
        }
        renumberHolders();
    }

    private void renumberHolders() {
        for(int i = 0; i < viewHolders.size(); i++) {
            viewHolders.get(i).setNumber(i + 1);
        }
    }

    @Nonnull
    public ImmutableList<T> getValues() {
        return objectPresenters.stream()
                               .map(ObjectPresenter::getValue)
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(toImmutableList());
    }
}
