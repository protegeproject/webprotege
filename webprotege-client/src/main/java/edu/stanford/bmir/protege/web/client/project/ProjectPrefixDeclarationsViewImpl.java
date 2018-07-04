package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public class ProjectPrefixDeclarationsViewImpl extends Composite implements ProjectPrefixDeclarationsView {

    interface ProjectPrefixesViewImplUiBinder extends UiBinder<HTMLPanel, ProjectPrefixDeclarationsViewImpl> {
    }

    private static ProjectPrefixesViewImplUiBinder ourUiBinder = GWT.create(ProjectPrefixesViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<PrefixDeclaration> prefixesEditor;

    @Inject
    public ProjectPrefixDeclarationsViewImpl() {
        prefixesEditor = new ValueListFlexEditorImpl<>(PrefixDeclarationEditor::new);
        initWidget(ourUiBinder.createAndBindUi(this));
        prefixesEditor.setEnabled(true);
    }

    @UiHandler("prefixesEditor")
    protected void handlePrefixDeclarationsChanged(ValueChangeEvent<Optional<List<PrefixDeclaration>>> evt) {
        ValueChangeEvent.fire(this, prefixesEditor.getValue().orElse(Collections.emptyList()));
    }

    @Override
    public void clear() {
        prefixesEditor.clearValue();
    }

    @Override
    public void setPrefixDeclarations(@Nonnull List<PrefixDeclaration> prefixDeclarations) {
        prefixesEditor.clearValue();
        prefixesEditor.setValue(prefixDeclarations);
    }

    @Nonnull
    @Override
    public List<PrefixDeclaration> getPrefixDeclarations() {
        return prefixesEditor.getValue().orElse(Collections.emptyList());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<PrefixDeclaration>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}