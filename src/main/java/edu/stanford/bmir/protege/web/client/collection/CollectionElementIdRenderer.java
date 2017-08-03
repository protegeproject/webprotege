package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2017
 */
public class CollectionElementIdRenderer extends AbstractCell<CollectionElementId> {


    interface CollectionElementIdRendererUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, CollectionElementId value);

        void onBrowserEvent(CollectionElementIdRenderer cell,
                            NativeEvent event,
                            Element parent,
                            CollectionElementId value);
    }

    private static CollectionElementIdRendererUiRenderer ourUiRenderer = GWT.create(
            CollectionElementIdRendererUiRenderer.class);

    public CollectionElementIdRenderer() {
        super();
    }

    @Override
    public void render(Context context, CollectionElementId value, SafeHtmlBuilder builder) {
        ourUiRenderer.render(builder, value);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, CollectionElementId value,
                               NativeEvent event, ValueUpdater<CollectionElementId> updater) {
        ourUiRenderer.onBrowserEvent(this, event, parent, value);
    }
}