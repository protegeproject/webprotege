package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaViewImpl extends Composite implements TagCriteriaView {

    interface TagCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaViewImpl> {

    }

    private static TagCriteriaViewImplUiBinder ourUiBinder = GWT.create(TagCriteriaViewImplUiBinder.class);

    @UiField
    protected SimplePanel tagCriteriaContainer;

    @UiField
    ListBox availableTagsField;

    @Inject
    public TagCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTagCriteriaContainer() {
        return tagCriteriaContainer;
    }

    @Override
    public void setAvailableTagLabels(@Nonnull List<String> tagLabels) {
        String sel = availableTagsField.getSelectedValue();
        availableTagsField.clear();
        availableTagsField.addItem("");
        int index = -1;
        for(int i = 0; i < tagLabels.size(); i++) {
            String tagName = tagLabels.get(i);
            availableTagsField.addItem(tagName);
            if(tagName.equals(sel)) {
                // Off set because of empty tag
                index = i + 1;
            }
        }
        if(index != -1) {
            availableTagsField.setSelectedIndex(index);
        }
    }

    @Nonnull
    @Override
    public Optional<String> getSelectedTagLabel() {
        return Optional.ofNullable(availableTagsField.getSelectedValue())
                       .filter(tn -> !tn.isEmpty());
    }

    @Override
    public void setSelectedTagLabel(@Nonnull String selectedTagLabel) {
        for(int i = 0; i < availableTagsField.getItemCount(); i++) {
            String value = availableTagsField.getValue(i);
            if(selectedTagLabel.equals(value)) {
                availableTagsField.setSelectedIndex(i);
                break;
            }
        }
    }
}