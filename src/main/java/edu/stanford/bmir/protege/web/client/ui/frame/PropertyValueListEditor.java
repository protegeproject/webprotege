package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueListEditor extends Composite implements ValueEditor<PropertyValueList>, HasEnabled {

    private ValueListEditor<PropertyValueDescriptor> editor;

    private ProjectId projectId;

    private PropertyValueGridGrammar grammar = PropertyValueGridGrammar.getClassGrammar();

    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public PropertyValueListEditor(ProjectId projectId, final Provider<PrimitiveDataEditorImpl> primitiveDataEditorProvider, DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.editor = new ValueListEditorImpl<PropertyValueDescriptor>(
                new ValueEditorFactory<PropertyValueDescriptor>() {
                    @Override
                    public ValueEditor<PropertyValueDescriptor> createEditor() {
                        PropertyValueDescriptorEditorImpl propertyValueEditor = new PropertyValueDescriptorEditorImpl(primitiveDataEditorProvider.get(), primitiveDataEditorProvider.get());
                        PropertyValueDescriptorEditorPresenter presenter = new PropertyValueDescriptorEditorPresenter(propertyValueEditor);
                        presenter.setGrammar(grammar);
                        return presenter;
                    }
                }
        );
        initWidget(editor.asWidget());
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<List<PropertyValueDescriptor>>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<List<PropertyValueDescriptor>>> event) {
                GWT.log("Received ValueChangeEvent from The ValueListEditor.  Firing the event for the PropertyValueListEditor.");
                ValueChangeEvent.fire(PropertyValueListEditor.this, getValue());
            }
        });
    }


    public void setGrammar(PropertyValueGridGrammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public void setValue(final PropertyValueList propertyValueList) {
        dispatchServiceManager.execute(new GetEntityDataAction(projectId, ImmutableSet.copyOf(propertyValueList.getSignature())), new DispatchServiceCallback<GetEntityDataResult>() {
            @Override
            public void handleSuccess(final GetEntityDataResult result) {
                fillUp(propertyValueList, new HasEntityDataProvider() {
                    @Override
                    public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
                        return Optional.of(result.getEntityDataMap().get(entity));
                    }
                });
            }
        });
    }

    private void fillUp(PropertyValueList propertyValueList, HasEntityDataProvider provider) {
        GWT.log("Filling the editor");
        List<PropertyValueDescriptor> vals = Lists.newArrayList();
        for (PropertyValue propertyValue : propertyValueList.getPropertyValues()) {
            if (propertyValue.getState() == PropertyValueState.ASSERTED) {
                Optional<PropertyValueDescriptor> val =    addRelationship(propertyValue, provider);
                if (val.isPresent()) {
                    vals.add(val.get());
                }
            }
        }
        editor.setValue(vals);
    }



    private Optional<PropertyValueDescriptor> addRelationship(final PropertyValue propertyValue, HasEntityDataProvider provider) {
        final Optional<OWLEntityData> propRendering = provider.getEntityData(propertyValue.getProperty());
        OWLPrimitiveData valueRendering = null;
        if (propertyValue.getValue() instanceof OWLEntity) {
            final Optional<OWLEntityData> propertyData = provider.getEntityData((OWLEntity) propertyValue.getValue());
            valueRendering = propertyData.get();
        }
        else if (propertyValue.getValue() instanceof OWLLiteral) {
            valueRendering = new OWLLiteralData((OWLLiteral) propertyValue.getValue());
        }
        else if (propertyValue.getValue() instanceof IRI) {
            valueRendering = new IRIData((IRI) propertyValue.getValue());
        }
        if(propRendering.isPresent()) {
            return Optional.of(new PropertyValueDescriptor((OWLPropertyData) propRendering.get(), valueRendering, propertyValue.getState(), propertyValue.isValueMostSpecific(), Collections.<OWLAxiom>emptySet()));
        }
        else {
            return Optional.absent();
        }
//        addRow(propRendering.isPresent() ? Optional.<OWLPrimitiveData>of(propRendering.get()) : Optional.<OWLPrimitiveData>absent(), Optional.of(valueRendering), propertyValue.getState(), propertyValue.isValueMostSpecific());
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<PropertyValueList> getValue() {
        Optional<List<PropertyValueDescriptor>> value =  editor.getValue();
        if(!value.isPresent()) {
            return Optional.absent();
        }
        List<PropertyValue> propertyValues = Lists.newArrayList();
        for(PropertyValueDescriptor val : value.get()) {
            Optional<PropertyValue> propertyValue = val.toPropertyValue();
            if (propertyValue.isPresent()) {
                propertyValues.add(propertyValue.get());
            }
        }
        return Optional.of(new PropertyValueList(propertyValues));
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    @Override
    public boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return editor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PropertyValueList>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return editor.isWellFormed();
    }

}
