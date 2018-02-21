package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class CommentedEntityDataRenderer extends AbstractCell<CommentedEntityData> {


    interface CommentedEntityDataRendererUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, CommentedEntityDataRendering value);

        void onBrowserEvent(CommentedEntityDataRenderer cell, NativeEvent event, Element parent, CommentedEntityData value);
    }

    private static CommentedEntityDataRendererUiRenderer ourUiRenderer = GWT.create(CommentedEntityDataRendererUiRenderer.class);

    public CommentedEntityDataRenderer() {
        super();
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

}