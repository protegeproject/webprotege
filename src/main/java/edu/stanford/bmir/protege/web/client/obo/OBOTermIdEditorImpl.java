package edu.stanford.bmir.protege.web.client.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/06/2013
 */
public class OBOTermIdEditorImpl extends Composite implements OBOTermIdEditor {

    interface OBOTermIdEditorImplUiBinder extends UiBinder<HTMLPanel, OBOTermIdEditorImpl> {

    }


    @UiField
    protected TextBox idField;

    @UiField
    protected TextBox nameField;

    @UiField(provided = true)
    protected SuggestBox namespaceField;


    private boolean dirty = false;

    private static OBOTermIdEditorImplUiBinder ourUiBinder = GWT.create(OBOTermIdEditorImplUiBinder.class);



    private Set<OBONamespace> namespaces = new HashSet<OBONamespace>();


    public OBOTermIdEditorImpl() {
        namespaceField = new SuggestBox(new NamespaceSuggestOracle());
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Override
    public void setValue(OBOTermId object) {
        idField.setValue(object.getId());
        nameField.setValue(object.getName());
        namespaceField.setValue(object.getNamespace());
        dirty = false;
    }

    @Override
    public void clearValue() {
        idField.setValue("");
        nameField.setValue("");
        namespaceField.setValue("");
        dirty = false;
    }

    @Override
    public Optional<OBOTermId> getValue() {
        if(getId().isEmpty()) {
            return Optional.absent();
        }
        if(getName().isEmpty()) {
            return Optional.absent();
        }
        if(getNamespace().isEmpty()) {
            return Optional.of(new OBOTermId(getId(), getName()));
        }
        else {
            return Optional.of(new OBOTermId(getId(), getName(), getNamespace()));
        }
    }

    @Override
    public void setAvailableNamespaces(Set<OBONamespace> namespaces) {
        this.namespaces.clear();
        this.namespaces.addAll(namespaces);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBOTermId>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }




    private String getNamespace() {
        return namespaceField.getValue().trim();
    }

    private String getName() {
        return nameField.getValue().trim();
    }

    private String getId() {
        return idField.getValue().trim();
    }




    private class NamespaceSuggestOracle extends SuggestOracle {

        @Override
        public void requestSuggestions(Request request, Callback callback) {
            String query = request.getQuery().toLowerCase();
            Set<Suggestion> suggestions = new HashSet<Suggestion>();
            for(final OBONamespace ns : namespaces) {
                if(ns.getNamespace().toLowerCase().startsWith(query)) {
                    suggestions.add(new Suggestion() {
                        public String getDisplayString() {
                            return ns.getNamespace();
                        }

                        public String getReplacementString() {
                            return ns.getNamespace();
                        }
                    });
                }
            }
            callback.onSuggestionsReady(request, new Response(suggestions));
        }
    }
}
