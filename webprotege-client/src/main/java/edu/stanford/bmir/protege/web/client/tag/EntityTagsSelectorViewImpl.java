package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2018
 */
public class EntityTagsSelectorViewImpl extends Composite implements EntityTagsSelectorView {

    interface EntityTagsSelectorViewImplUiBinder extends UiBinder<HTMLPanel, EntityTagsSelectorViewImpl> {
    }

    private static EntityTagsSelectorViewImplUiBinder ourUiBinder = GWT.create(EntityTagsSelectorViewImplUiBinder.class);
    @UiField
    HTMLPanel container;

    @Inject
    public EntityTagsSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void addCheckBox(EntityTagCheckBox tagCheckBox) {
        container.add(tagCheckBox);
    }

    @Override
    public List<EntityTagCheckBox> getCheckBoxes() {
        List<EntityTagCheckBox> checkBoxes = new ArrayList<>();
        for(int i = 0; i < container.getWidgetCount(); i++) {
            Widget widget = container.getWidget(i);
            if (widget instanceof EntityTagCheckBox) {
                checkBoxes.add((EntityTagCheckBox) widget);
            }
        }
        return checkBoxes;
    }
}