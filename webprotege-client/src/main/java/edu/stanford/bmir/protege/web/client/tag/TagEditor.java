package edu.stanford.bmir.protege.web.client.tag;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.color.ColorSwatchPresenter;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.tag.TagData;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
public class TagEditor extends Composite implements ValueEditor<TagData>
{

    interface TagEditorUiBinder extends UiBinder<HTMLPanel, TagEditor> {
    }

    private static TagEditorUiBinder ourUiBinder = GWT.create(TagEditorUiBinder.class);

    private Optional<TagId> tagId = Optional.empty();

    private boolean enabled = true;

    @Nonnull
    private final ColorSwatchPresenter colorSwatchPresenter;

    @UiField
    TextBox tagLabelField;

    @UiField
    TextBox descriptionField;

    @UiField
    HTMLPanel backgroundColorField;

    @UiField
    FocusPanel backgroundColorFocusPanel;

    @UiField
    Label usageField;

    private Color backgroundColor = Color.getRGB(0, 0, 0);

    private int usage = 0;

    @Inject
    public TagEditor(@Nonnull ColorSwatchPresenter colorSwatchPresenter) {
        this.colorSwatchPresenter = checkNotNull(colorSwatchPresenter);
        this.colorSwatchPresenter.setColorSelectedHandler(this::handleColorSelected);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("tagLabelField")
    protected void handleTagLabelChanged(ValueChangeEvent<String> event) {
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("descriptionField")
    protected void handleDescriptionFieldChanged(ValueChangeEvent<String> event) {
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("backgroundColorFocusPanel")
    protected void handleBackgroundColorClicked(ClickEvent event) {
        colorSwatchPresenter.showPopup(backgroundColorField);
    }

    private void handleColorSelected(@Nonnull Color color) {
        backgroundColor = color;
        backgroundColorField.getElement().getStyle().setBackgroundColor(color.getHex());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        setRandomBackgroundColor();

    }

    private void setRandomBackgroundColor() {
        // Generate a random background color.  Note that we don't want
        // the color to be too feint.
        int r = Random.nextInt(210);
        int g = Random.nextInt(210);
        int b = Random.nextInt(210);
        Color c = Color.getRGB(r, g, b);
        backgroundColor = c;
        backgroundColorField.getElement().getStyle().setBackgroundColor(c.getHex());
    }

    @Override
    public void setValue(TagData tag) {
        this.tagId = tag.getTagId();
        tagLabelField.setValue(tag.getLabel());
        descriptionField.setValue(tag.getDescription());
        backgroundColor = tag.getBackgroundColor();
        backgroundColorField.getElement().getStyle().setBackgroundColor(tag.getBackgroundColor().getHex());
        if (tag.getUsageCount() > 0) {
            usageField.setText("(" + tag.getUsageCount() + " tagged)");
        }
        usage = tag.getUsageCount();
    }

    @Override
    public void clearValue() {
        tagId = Optional.empty();
        tagLabelField.setValue("");
        descriptionField.setValue("");
        backgroundColor = Color.getRGB(0, 0, 0);
        backgroundColorField.getElement().getStyle().clearBackgroundColor();
        usageField.setText("");
        usage = 0;
    }

    @Override
    public Optional<TagData> getValue() {
        if(getTagLabel().isEmpty()) {
            return Optional.empty();
        }
        TagData tagData = TagData.get(tagId.orElse(null),
                                      getTagLabel(),
                                      descriptionField.getValue().trim(),
                                      Color.getWhite(),
                                      backgroundColor,
                                      ImmutableList.of(),
                                      usage);
        return Optional.of(tagData);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<TagData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Nonnull
    private String getTagLabel() {
        return tagLabelField.getValue().trim();
    }
}