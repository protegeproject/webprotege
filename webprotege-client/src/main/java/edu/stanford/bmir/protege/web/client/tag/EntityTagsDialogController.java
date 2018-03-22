package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2018
 */
public class EntityTagsDialogController extends WebProtegeOKCancelDialogController<List<Tag>> {

    private final EntityTagsSelectorPresenter presenter;

    @Inject
    @Nonnull
    public EntityTagsDialogController(@Nonnull EntityTagsSelectorPresenter presenter) {
        super("Entity Tags");
        this.presenter = checkNotNull(presenter);
        setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            presenter.saveSelectedTags();
            closer.hide();
        });

    }

    public void start(@Nonnull OWLEntity entity) {
        presenter.start(entity);
    }

    @Override
    public Widget getWidget() {
        return presenter.getView().asWidget();
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.empty();
    }

    @Override
    public List<Tag> getData() {
        return presenter.getSelectedTags();
    }
}

