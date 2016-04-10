package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/04/16
 */
public class ImageFieldEditor extends Composite implements ValueEditor<FormDataValue> {

    interface ImageFieldEditorUiBinder extends UiBinder<HTMLPanel, ImageFieldEditor> {

    }

    private static ImageFieldEditorUiBinder ourUiBinder = GWT.create(ImageFieldEditorUiBinder.class);

    @UiField
    Image imageField;

    @UiField
    Label placeHolder;

    @UiField
    HTMLPanel container;

    private Optional<IRI> theIRI = Optional.absent();

    private boolean dirty = false;

    public ImageFieldEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
        updateUi();
    }

    @Override
    public void setValue(FormDataValue object) {
        theIRI = object.asIRI();
        updateUi();
    }

    private void updateUi() {
        if (theIRI.isPresent()) {
            imageField.setUrl(theIRI.get().toString());
            imageField.getElement().getStyle().clearDisplay();
            placeHolder.setVisible(false);
        }
        else {
            imageField.getElement().getStyle().setDisplay(Style.Display.NONE);
            placeHolder.setVisible(true);
        }
    }

    @UiHandler("imageField")
    protected void handleImageClicked(ClickEvent event) {
        showEditingDialog();
    }

    @UiHandler("placeHolder")
    protected void handlePlaceHolderClicked(ClickEvent event) {
        showEditingDialog();
    }

    private void showEditingDialog() {
        InputBox.showDialog(
                "Image URL",
                false,
                theIRI.transform(iri -> iri.toString()).or(""),
                input -> {
                    Optional<IRI> newValue = Optional.of(IRI.create(input));
                    if (!theIRI.equals(newValue)) {
                        theIRI = newValue;
                        imageField.setUrl(input);
                        updateUi();
                        ValueChangeEvent.fire(ImageFieldEditor.this, getValue());
                    }
                });
    }

    @Override
    public void clearValue() {
        theIRI = Optional.absent();
        dirty = false;
        updateUi();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        return theIRI.transform((iri) -> FormDataPrimitive.get(iri));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}