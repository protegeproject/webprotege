package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentViewImpl extends Composite implements MoveToParentView {

    interface MoveToParentViewImplUiBinder extends UiBinder<HTMLPanel, MoveToParentViewImpl> {

    }

    private static MoveToParentViewImplUiBinder ourUiBinder = GWT.create(MoveToParentViewImplUiBinder.class);


    @UiField
    SimplePanel hierarchyFieldContainer;

    @Inject
    public MoveToParentViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyFieldContainer() {
        return hierarchyFieldContainer;
    }

    public void requestFocus() {
        if(hierarchyFieldContainer.getWidget() instanceof HasRequestFocus) {
            ((HasRequestFocus) hierarchyFieldContainer.getWidget()).requestFocus();
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        requestFocus();
    }
}