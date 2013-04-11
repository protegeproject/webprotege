package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBONamespace;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermIdEditor implements OBOTermEditor {

    private TextBox idField = new TextBox();

    private TextBox nameField = new TextBox();

    private SuggestBox namespaceField;
    
    
    private List<String> labels = new ArrayList<String>(3);
    
    private List<Widget> widgets = new ArrayList<Widget>(3);
    
    
    private Set<OBONamespace> namespaces = new HashSet<OBONamespace>();
    
    private boolean dirty = false;


    public OBOTermIdEditor() {
        labels.add("ID");
        labels.add("Name");
        labels.add("Namespace");

        namespaceField = new SuggestBox(new NamespaceSuggestOracle());
        
        widgets.add(idField);
        idField.setEnabled(false);
        ChangeHandler dirtyChangeHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                dirty = true;
            }
        };
        idField.addChangeHandler(dirtyChangeHandler);
        idField.setVisibleLength(70);
        widgets.add(nameField);
        nameField.addChangeHandler(dirtyChangeHandler);
        nameField.setVisibleLength(70);
        widgets.add(namespaceField);
        namespaceField.setWidth("100%");
        namespaceField.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
            }
        });

    }


    public void setAvailableNamespaces(Set<OBONamespace> namespaces) {
        this.namespaces = namespaces;
    }

    public void setValue(OBOTermId termId) {
        idField.setValue(termId.getId());
        nameField.setValue(termId.getName());
        namespaceField.setValue(termId.getNamespace());
        dirty = false;
    }

    public void clearValue() {
        idField.setValue("");
        nameField.setValue("");
        namespaceField.setValue("");
        dirty = false;
    }
    
    public OBOTermId getValue() {
        return new OBOTermId(idField.getValue().trim(), nameField.getValue().trim(), namespaceField.getValue().trim());
    }

    public boolean isDirty() {
        return dirty;
    }

    public int getEditorCount() {
        return 3;
    }

    public String getLabel(int index) {
        return labels.get(index);
    }

    public Widget getEditorWidget(int index) {
        Widget widget = widgets.get(index);
        return widget;
    }

    public String getHeight(int index) {
        return "1.1em";
    }

    public boolean hasXRefs() {
        return false;
    }

    public XRefListEditor getXRefListEditor() {
        return null;
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
