package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.place.ProjectPrefixDeclarationsPlace;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2018
 */
public class EditProjectPrefixDeclarationsHandlerImpl implements EditProjectPrefixDeclarationsHandler {

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public EditProjectPrefixDeclarationsHandlerImpl(@Nonnull PlaceController placeController) {
        this.placeController = checkNotNull(placeController);
    }

    @Override
    public void handleEditProjectPrefixes() {
        Place currentPlace = placeController.getWhere();
        if(currentPlace instanceof HasProjectId) {
            ProjectId projectId = ((HasProjectId) currentPlace).getProjectId();
            ProjectPrefixDeclarationsPlace prefixesPlace = new ProjectPrefixDeclarationsPlace(projectId, Optional.ofNullable(currentPlace));
            placeController.goTo(prefixesPlace);
        }
    }
}
