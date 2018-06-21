package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaListViewImpl extends Composite implements TagCriteriaListView {

    private AddHandler addHandler = () -> {};

    interface TagCriteriaListViewImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaListViewImpl> {

    }

    private static TagCriteriaListViewImplUiBinder ourUiBinder = GWT.create(TagCriteriaListViewImplUiBinder.class);

    @UiField
    HTMLPanel tagCriteriaListContainer;

    @UiField
    Button addButton;

    @Inject
    public TagCriteriaListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setAddHandler(@Nonnull AddHandler handler) {
        this.addHandler = checkNotNull(handler);
    }

    @Override
    public void clearView() {
        tagCriteriaListContainer.clear();
    }

    @Override
    public void addTagCriteriaViewContainer(@Nonnull TagCriteriaViewContainer container,
                                            boolean scrollIntoView) {
        tagCriteriaListContainer.add(container);
        if (scrollIntoView) {
            addButton.getElement().scrollIntoView();
        }
    }

    @Override
    public void removeTagCriteriaViewContainer(int viewContainerIndex) {
        tagCriteriaListContainer.remove(viewContainerIndex);
    }

    @UiHandler("addButton")
    public void handleAddButtonClicked(ClickEvent event) {
        addHandler.handleAdd();
    }
}