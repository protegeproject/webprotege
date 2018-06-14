package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public interface SelectableCriteriaTypeView extends IsWidget, AcceptsOneWidget {


    interface SelectedNameChangedHander {
        void handleSelectedNameChanged();
    }


    int getSelectedIndex();

    void setSelectableNames(@Nonnull List<String> names);

    void setSelectedName(int index);

    @Nonnull
    Optional<String> getSelectedName();

    void setSelectedNameChangedHandler(@Nonnull SelectedNameChangedHander handler);

}
