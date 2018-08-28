package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasAcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public class CreateEntitiesDialogViewImpl extends Composite implements  CreateEntityDialogView, HasAcceptKeyHandler {

    interface CreateEntitiesDialogViewImplUiBinder extends UiBinder<HTMLPanel, CreateEntitiesDialogViewImpl> {
    }

    private static CreateEntitiesDialogViewImplUiBinder ourUiBinder = GWT.create(CreateEntitiesDialogViewImplUiBinder.class);

    @UiField
    ExpandingTextBoxImpl textBox;

    @UiField(provided = true)
    DefaultLanguageEditor langField;

    @UiField
    Label entityNamesLabel;

    @UiField
    Button resetButton;

    private ResetLangTagHandler resetLangTagHandler = () -> {};


    @Inject
    public CreateEntitiesDialogViewImpl(DefaultLanguageEditor languageEditor) {
        this.langField = checkNotNull(languageEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        entityNamesLabel.setText(entityType.getPrintName() + " names");
    }

    @Nonnull
    @Override
    public String getText() {
        return textBox.getText().trim();
    }

    @Override
    public void clear() {
        textBox.setText("");
    }

    @Override
    public void setResetLangTagHandler(@Nonnull ResetLangTagHandler handler) {
        this.resetLangTagHandler = checkNotNull(handler);
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> textBox.setFocus(true));
    }

    @Override
    public void setAcceptKeyHandler(AcceptKeyHandler acceptKey) {
        textBox.setAcceptKeyHandler(acceptKey);
    }

    @Nonnull
    @Override
    public String getLangTag() {
        return langField.getValue().orElse("");
    }

    @Override
    public void setLangTag(@Nonnull String langTag) {
        langField.setValue(checkNotNull(langTag));
    }

    @UiHandler("resetButton")
    public void resetButtonClick(ClickEvent event) {
        resetLangTagHandler.handleResetLangTag();
    }
}