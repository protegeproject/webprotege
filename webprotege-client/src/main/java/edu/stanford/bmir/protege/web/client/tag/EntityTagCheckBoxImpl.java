package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class EntityTagCheckBoxImpl extends Composite implements EntityTagCheckBox {

    interface EntityTagCheckBoxImplUiBinder extends UiBinder<HTMLPanel, EntityTagCheckBoxImpl> {
    }

    private static EntityTagCheckBoxImplUiBinder ourUiBinder = GWT.create(EntityTagCheckBoxImplUiBinder.class);

    @UiField
    CheckBox checkBox;

    @UiField
    TagViewImpl tagView;

    public EntityTagCheckBoxImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("tagView")
    protected void handleMouseUp(MouseUpEvent event) {
        checkBox.setValue(!checkBox.getValue());
    }

    @Nullable
    private Tag tag;

    @Override
    public void setTag(@Nonnull Tag tag) {
        this.tag = checkNotNull(tag);
        tagView.setTagId(tag.getTagId());
        tagView.setLabel(tag.getLabel());
        tagView.setDescription(tag.getDescription());
    }

    @Nonnull
    @Override
    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    @Override
    public void setSelected(boolean selected) {
        checkBox.setValue(selected);
    }

    @Override
    public boolean isSelected() {
        return checkBox.getValue();
    }
}