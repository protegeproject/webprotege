package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormControlDescriptorViewImpl extends Composite implements SubFormControlDescriptorView {

    @Override
    public void clear() {

    }

    @UiField
    protected SimplePanel subjectFactoryDescriptorContainer;

    interface SubFormControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, SubFormControlDescriptorViewImpl> {

    }

    private static SubFormControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            SubFormControlDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel subFormContainer;

    @Inject
    public SubFormControlDescriptorViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSubFormContainer() {
        return subFormContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormSubjectDescriptorViewContainr() {
        return subjectFactoryDescriptorContainer;
    }
}
