package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.place.ProjectTagsPlace;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Mar 2018
 */
public class EditProjectTagsUIActionHandler {

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public EditProjectTagsUIActionHandler(@Nonnull PlaceController placeController) {
        this.placeController = placeController;
    }

    public void handleEditProjectTags() {
        Place currentPlace = placeController.getWhere();
        if(!(currentPlace instanceof HasProjectId)) {
            return;
        }
        ProjectId projectId = ((HasProjectId) currentPlace).getProjectId();
        ProjectTagsPlace projectTagsPlace = new ProjectTagsPlace(projectId,
                                                                 Optional.of(currentPlace));
        placeController.goTo(projectTagsPlace);
    }

}
