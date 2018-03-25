package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle;
import edu.stanford.bmir.protege.web.client.color.ColorSwatchPresenter;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
@SuppressWarnings("Convert2MethodRef")
public class ProjectTagsViewImpl extends Composite implements ProjectTagsView {

    interface ProjectTagsViewImplUiBinder extends UiBinder<HTMLPanel, ProjectTagsViewImpl> {
    }

    private static ProjectTagsViewImplUiBinder ourUiBinder = GWT.create(ProjectTagsViewImplUiBinder.class);

    @UiField
    Button cancelButton;

    @UiField
    Button applyButton;

    @UiField
    Label projectTitle;

    @UiField(provided = true)
    ValueListEditor<TagData> tagsEditor;

    private ApplyChangesHandler applyChangesHandler = () -> {};

    private CancelChangesHandler cancelChangesHandler = () -> {};

    @Inject
    public ProjectTagsViewImpl(@Nonnull Provider<ColorSwatchPresenter> colorSwatchPresenter) {
        tagsEditor = new ValueListFlexEditorImpl<>(() -> new TagEditor(colorSwatchPresenter.get()));
        tagsEditor.setDeleteConfirmationPrompt((value, callback) -> {
            MessageBox.showConfirmBox(MessageStyle.QUESTION,
                                      "Delete tag",
                                      "Are you sure that you want to delete the " + value.getLabel() + " tag?",
                                      DialogButton.CANCEL,
                                      () -> callback.deleteValue(false),
                                      DialogButton.get("Delete Tag"),
                                      () -> callback.deleteValue(true),
                                      DialogButton.CANCEL);
        });
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        tagsEditor.setEnabled(true);
    }

    @UiHandler("applyButton")
    protected void handleApplyChanges(ClickEvent event) {
        applyChangesHandler.handleApplyChanges();
    }

    @UiHandler("cancelButton")
    protected void handleCancelChanges(ClickEvent event) {
        cancelChangesHandler.handleCancelChanges();
    }

    @Override
    public void setProjectTitle(@Nonnull String projectTitle) {
        this.projectTitle.setText(checkNotNull(projectTitle));
    }

    @Override
    public void setApplyChangesHandler(@Nonnull ApplyChangesHandler applyChangesHandler) {
        this.applyChangesHandler = checkNotNull(applyChangesHandler);
    }

    @Override
    public void setCancelChangesHandler(@Nonnull CancelChangesHandler cancelChangesHandler) {
        this.cancelChangesHandler = checkNotNull(cancelChangesHandler);
    }

    @Override
    public void setCancelButtonVisible(boolean visible) {
        cancelButton.setVisible(visible);
    }

    @Override
    public void setTags(List<Tag> tags) {
        List<TagData> tagData = tags.stream()
                                    .map(tag -> new TagData(Optional.of(tag.getTagId()),
                                                            tag.getLabel(),
                                                            tag.getDescription(),
                                                            tag.getColor(),
                                                            tag.getBackgroundColor()))
                                    .collect(toList());
        tagsEditor.setValue(tagData);
    }

    @Override
    public List<TagData> getTagData() {
        return tagsEditor.getValue()
                         .orElse(emptyList());
    }
}