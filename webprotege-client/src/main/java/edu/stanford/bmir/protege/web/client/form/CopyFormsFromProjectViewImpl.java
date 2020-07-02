package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.client.project.ProjectDetailsListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public class CopyFormsFromProjectViewImpl extends Composite implements CopyFormsFromProjectView {

    private SelectedProjectChangedHandler selectedProjectChangedHandler = () -> {};

    interface CopyFormsFromProjectViewImplUiBinder extends UiBinder<HTMLPanel, CopyFormsFromProjectViewImpl> {

    }

    private static CopyFormsFromProjectViewImplUiBinder ourUiBinder = GWT.create(CopyFormsFromProjectViewImplUiBinder.class);

    @UiField
    ListBox<ProjectId, ProjectDetails> projectsList;

    @UiField
    ListBox<FormId, FormDescriptor> projectFormsList;

    @Inject
    public CopyFormsFromProjectViewImpl(@Nonnull ProjectDetailsListBoxCellRenderer renderer,
                                        @Nonnull Provider<FormIdPresenter> formIdPresenterProvider) {
        initWidget(ourUiBinder.createAndBindUi(this));
        projectsList.setRenderer(renderer);
        projectFormsList.setRenderer(element -> {
            FormIdPresenter presenter = formIdPresenterProvider.get();
            SimplePanel container = new SimplePanel();
            presenter.start(container);
            presenter.setFormDescriptor(element);
            return container;
        });
    }

    @Override
    public void setProjects(@Nonnull List<ProjectDetails> projects) {
        projectsList.setListData(projects);
        if(projects.size() > 1) {
            projectsList.setSelectionInterval(ListBox.SelectionInterval.singleRow(0));
            selectedProjectChangedHandler.handleSelectedProjectChanged();
        }
        projectsList.addSelectionHandler(event -> selectedProjectChangedHandler.handleSelectedProjectChanged());
    }

    @Override
    public void setSelectedProjectChangedHandler(@Nonnull SelectedProjectChangedHandler handler) {
        this.selectedProjectChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(projectsList);
    }

    @Nonnull
    @Override
    public Optional<ProjectId> getSelectedProjectId() {
        return projectsList.getFirstSelectedElement()
                .map(ProjectDetails::getProjectId);
    }

    @Override
    public void setFormDescriptors(@Nonnull List<FormDescriptor> formDescriptors) {
        projectFormsList.setListData(formDescriptors);
    }

    @Nonnull
    @Override
    public List<FormDescriptor> getSelectedFormIds() {
        return projectFormsList.getSelection();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        projectsList.requestFocus();
    }
}
