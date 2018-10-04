package edu.stanford.bmir.protege.web.client.tag;

import com.google.common.collect.ImmutableList;
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

    @UiField(provided = true)
    ValueListEditor<TagData> tagsEditor;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    private TagListChangedHandler tagListChangedHandler = () -> {};

    @Inject
    public ProjectTagsViewImpl(@Nonnull Provider<ColorSwatchPresenter> colorSwatchPresenter,
                               @Nonnull Messages messages,
                               @Nonnull DeleteProjectTagConfirmationPrompt deletePrompt,
                               @Nonnull MessageBox messageBox) {
        tagsEditor = new ValueListFlexEditorImpl<>(() -> new TagEditor(colorSwatchPresenter.get()));
        this.messages = checkNotNull(messages);
        this.messageBox = messageBox;
        tagsEditor.setDeleteConfirmationPrompt(deletePrompt);
        tagsEditor.addValueChangeHandler(event -> tagListChangedHandler.handleTagListChanged());
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        tagsEditor.setEnabled(true);
    }

    @Override
    public void setTagListChangedHandler(@Nonnull TagListChangedHandler handler) {
        this.tagListChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setTags(@Nonnull List<Tag> tags, Map<TagId, Integer> usageCount) {
        List<TagData> tagData = tags.stream()
                                    .map(tag -> TagData.get(tag.getTagId(),
                                                            tag.getLabel(),
                                                            tag.getDescription(),
                                                            tag.getColor(),
                                                            tag.getBackgroundColor(),
                                                            ImmutableList.of(),
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
        messageBox.showAlert(messages.tags_duplicateTag_Title(),
                             messages.tags_duplicateTag_Message(label));
    }
}