package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.frame.PropertyValueGridGrammar;
import edu.stanford.bmir.protege.web.client.frame.PropertyValueListEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormFieldDescriptorViewImpl extends Composite implements SubFormFieldDescriptorView {

    interface SubFormFieldDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, SubFormFieldDescriptorViewImpl> {

    }

    private static SubFormFieldDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            SubFormFieldDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel subFormContainer;

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    @UiField(provided = true)
    ValueListEditor<OWLPrimitiveData> parentsList;

    @UiField
    RadioButton classRadio;

    @UiField
    RadioButton individualRadio;

    @Inject
    public SubFormFieldDescriptorViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        counter.increment();
        parentsList = new ValueListFlexEditorImpl<>(() -> {
            PrimitiveDataEditor editor = primitiveDataEditorProvider.get();
            editor.setEnabled(true);
            editor.setClassesAllowed(true);
            return editor;
        });
        parentsList.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        parentsList.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSubFormContainer() {
        return subFormContainer;
    }

    @Nonnull
    @Override
    public EntityType<?> getEntityType() {
        if(individualRadio.getValue()) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else {
            return EntityType.CLASS;
        }
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            individualRadio.setValue(true);
        }
        else {
            classRadio.setValue(true);
        }
    }

    @Override
    public void setParents(@Nonnull List<OWLPrimitiveData> parents) {
        parentsList.setValue(parents);
    }

    @Override
    public void clear() {
        parentsList.clearValue();
    }

    @Nonnull
    @Override
    public List<OWLPrimitiveData> getParents() {
        return parentsList.getEditorValue().orElse(Collections.emptyList());
    }
}
