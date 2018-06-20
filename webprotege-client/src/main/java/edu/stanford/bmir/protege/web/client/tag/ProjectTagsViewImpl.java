package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle;
import edu.stanford.bmir.protege.web.client.color.ColorSwatchPresenter;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle.QUESTION;
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

    @UiField
    SimplePanel tagCriteriaContainer;

    @Nonnull
    private final Messages messages;

    private ApplyChangesHandler applyChangesHandler = () -> {};

    private CancelChangesHandler cancelChangesHandler = () -> {};

    private TagListChangedHandler tagListChangedHandler = () -> {};

    @Inject
    public ProjectTagsViewImpl(@Nonnull Provider<ColorSwatchPresenter> colorSwatchPresenter,
                               @Nonnull Messages messages,
                               @Nonnull DeleteProjectTagConfirmationPrompt deletePrompt) {
        tagsEditor = new ValueListFlexEditorImpl<>(() -> new TagEditor(colorSwatchPresenter.get()));
        this.messages = checkNotNull(messages);
        tagsEditor.setDeleteConfirmationPrompt(deletePrompt);
        tagsEditor.addValueChangeHandler(event -> tagListChangedHandler.handleTagListChanged());
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTagCriteriaContainer() {
        return tagCriteriaContainer;
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
    public void setTagListChangedHandler(@Nonnull TagListChangedHandler handler) {
        this.tagListChangedHandler = checkNotNull(handler);
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
    public void setTags(@Nonnull List<Tag> tags, Map<TagId, Integer> usageCount) {
        List<TagData> tagData = tags.stream()
                                    .map(tag -> new TagData(Optional.of(tag.getTagId()),
                                                            tag.getLabel(),
                                                            tag.getDescription(),
                                                            tag.getColor(),
                                                            tag.getBackgroundColor(),
                                                            usageCount.getOrDefault(tag.getTagId(), 0)))
                                    .collect(toList());
        tagsEditor.setValue(tagData);
    }

    @Nonnull
    @Override
    public List<TagData> getTagData() {
        return tagsEditor.getValue()
                         .orElse(emptyList());
    }

    @Override
    public void showDuplicateTagAlert(@Nonnull String label) {
        MessageBox.showAlert(messages.tags_duplicateTag_Title(),
                             messages.tags_duplicateTag_Message(label));
    }
}