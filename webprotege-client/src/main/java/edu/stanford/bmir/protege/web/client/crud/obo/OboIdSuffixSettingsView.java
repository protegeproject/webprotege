package edu.stanford.bmir.protege.web.client.crud.obo;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface OboIdSuffixSettingsView extends IsWidget {

    void clearView();

    void setTotalDigits(String totalDigits);

    @Nonnull
    String getTotalDigits();

    void setUserIdRanges(@Nonnull List<UserIdRange> ranges);

    @Nonnull
    List<UserIdRange> getUserIdRanges();
}
