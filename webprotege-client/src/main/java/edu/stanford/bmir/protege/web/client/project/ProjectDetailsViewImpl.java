package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public class ProjectDetailsViewImpl extends Composite implements ProjectDetailsView {

    interface ProjectDetailsViewImplUiBinder extends UiBinder<HTMLPanel, ProjectDetailsViewImpl> {

    }

    private static ProjectDetailsViewImplUiBinder ourUiBinder = GWT.create(ProjectDetailsViewImplUiBinder.class);

    @UiField
    Label displayNameField;

    @Inject
    public ProjectDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setDisplayName(@Nonnull String displayName) {
        displayNameField.setText(displayName);
    }

    @Override
    public void setDescription(@Nonnull String description) {
        displayNameField.setTitle(description);
    }

    @Override
    public void setOwner(@Nonnull UserId owner) {

    }
}
