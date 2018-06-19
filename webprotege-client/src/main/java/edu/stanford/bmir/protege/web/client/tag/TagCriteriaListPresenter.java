package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaListPresenter {

    @Nonnull
    private final TagCriteriaListView view;

    @Nonnull
    private final Provider<TagCriteriaPresenter> presenterProvider;

    @Nonnull
    private final Provider<TagCriteriaViewContainer> tagCriteriaViewContainerProvider;

    private final List<TagCriteriaPresenter> presenters = new ArrayList<>();

    @Inject
    public TagCriteriaListPresenter(@Nonnull TagCriteriaListView view,
                                    @Nonnull Provider<TagCriteriaPresenter> presenterProvider, @Nonnull Provider<TagCriteriaViewContainer> tagCriteriaViewContainerProvider) {
        this.view = checkNotNull(view);
        this.presenterProvider = checkNotNull(presenterProvider);
        this.tagCriteriaViewContainerProvider = checkNotNull(tagCriteriaViewContainerProvider);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        addFirst();
        addFirst();
    }

    private void addFirst() {
        addTagCriteriaView();
    }

    private void addTagCriteriaView() {
        TagCriteriaPresenter presenter = presenterProvider.get();
        presenters.add(presenter);
        TagCriteriaViewContainer container = tagCriteriaViewContainerProvider.get();
        presenter.start(container.getViewContainer());
        view.addTagCriteriaViewContainer(container);
    }
}
