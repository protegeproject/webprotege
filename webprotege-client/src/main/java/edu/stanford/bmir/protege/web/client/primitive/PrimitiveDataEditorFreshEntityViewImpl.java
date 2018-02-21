package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.semanticweb.owlapi.model.EntityType;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public class PrimitiveDataEditorFreshEntityViewImpl extends Composite implements PrimitiveDataEditorFreshEntityView {

    interface PrimitiveDataEditorFreshEntityViewImplUiBinder extends UiBinder<HTMLPanel, PrimitiveDataEditorFreshEntityViewImpl> {

    }

    private static PrimitiveDataEditorFreshEntityViewImplUiBinder ourUiBinder = GWT.create(PrimitiveDataEditorFreshEntityViewImplUiBinder.class);

    @UiField
    protected HasSafeHtml mainMessageField;

    @UiField
    protected HasWidgets anchorContainer;

    private Map<EntityType<?>, Anchor> entityTypeAnchorMap = new HashMap<EntityType<?>, Anchor>();

    @Inject
    public PrimitiveDataEditorFreshEntityViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        for (EntityType<?> entityType : EntityType.values()) {
            entityTypeAnchorMap.put(entityType, new Anchor("Add as " + entityType.getPrintName()));
        }
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<EntityType<?>> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public void setExpectedTypes(SafeHtml message, Set<EntityType<?>> suggestedTypes) {
        mainMessageField.setHTML(message);
        anchorContainer.clear();
        for (final EntityType<?> entityType : suggestedTypes) {
            Anchor anchor = entityTypeAnchorMap.get(entityType);
            anchorContainer.add(new SimplePanel(anchor));
            anchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SelectionEvent.fire(PrimitiveDataEditorFreshEntityViewImpl.this, entityType);
                }
            });
        }
    }
}