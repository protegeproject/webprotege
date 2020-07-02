package edu.stanford.bmir.protege.web.client.crud.supplied;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface SuppliedNameSuffixSettingsView extends IsWidget {

    void clearView();

    void setWhiteSpaceTreatment(@Nonnull WhiteSpaceTreatment whiteSpaceTreatment);

    @Nonnull
    WhiteSpaceTreatment getWhiteSpaceTreatment();
}
