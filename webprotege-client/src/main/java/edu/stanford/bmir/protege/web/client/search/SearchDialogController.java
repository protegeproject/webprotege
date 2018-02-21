package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogController;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CLOSE;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.SELECT;
import static java.util.Arrays.asList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchDialogController extends WebProtegeDialogController<Optional<OWLEntityData>> {

    private final SearchPresenter searchPresenter;

    private final SelectionModel selectionModel;

    @Inject
    public SearchDialogController(SearchPresenter searchPresenter, SelectionModel selectionModel) {
        super("Search", asList(CLOSE, SELECT), SELECT, CLOSE);
        this.searchPresenter = searchPresenter;
        this.selectionModel = selectionModel;
        setDialogButtonHandler(SELECT, (data, closer) -> {
            data.ifPresent(this::selectEntity);
            closer.hide();
        });
        setDialogButtonHandler(CLOSE, (data, closer) -> {
            GWT.log("CANCEL");
            closer.hide();
        });
    }

    public void setEntityTypes(EntityType<?> ... entityTypes) {
        searchPresenter.setEntityTypes(entityTypes);
        if(entityTypes.length == 1) {
            setTitle("Search for " + entityTypes[0].getPrintName());
        }
    }

    private void selectEntity(OWLEntityData entityData) {
        selectionModel.setSelection(entityData.getEntity());
    }

    @Override
    public Widget getWidget() {
        searchPresenter.start();
        return searchPresenter.getView().asWidget();
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return searchPresenter.getInitialFocusable();
    }

    @Override
    public Optional<OWLEntityData> getData() {
        return searchPresenter.getSelectedSearchResult();
    }
}
