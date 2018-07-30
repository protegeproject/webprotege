package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class CommentedEntityDataRenderer extends AbstractCell<CommentedEntityData> {

    interface GoToButtonHandler {
        void handleGoTo(@Nonnull OWLEntityData entityData);
    }

    interface CommentedEntityDataRendererUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, CommentedEntityDataRendering value);

        void onBrowserEvent(CommentedEntityDataRenderer cell, NativeEvent event, Element parent, CommentedEntityData value);
    }

    private static CommentedEntityDataRendererUiRenderer ourUiRenderer = GWT.create(CommentedEntityDataRendererUiRenderer.class);

    private GoToButtonHandler goToButtonHandler = (entityData) -> {};

    public CommentedEntityDataRenderer() {
        super(BrowserEvents.CLICK);
    }

    @Override
    public void render(Context context, CommentedEntityData value, SafeHtmlBuilder builder) {
        ourUiRenderer.render(builder,
                             new CommentedEntityDataRendering(value));
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, CommentedEntityData value,
                               NativeEvent event, ValueUpdater<CommentedEntityData> updater) {
        ourUiRenderer.onBrowserEvent(this, event, parent, value);
    }

    @UiHandler({"gotoButton"})
    void onRemovePersonClicked(ClickEvent event, Element parent, CommentedEntityData value) {
        goToButtonHandler.handleGoTo(value.getEntityData());
    }

    public void setGoToButtonHandler(@Nonnull GoToButtonHandler goToButtonHandler) {
        this.goToButtonHandler = checkNotNull(goToButtonHandler);
    }
}