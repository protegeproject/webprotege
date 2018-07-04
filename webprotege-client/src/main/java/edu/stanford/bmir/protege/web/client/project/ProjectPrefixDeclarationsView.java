package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public interface ProjectPrefixDeclarationsView extends IsWidget, HasValueChangeHandlers<List<PrefixDeclaration>> {

    /**
     * Clears the view of any prefixes
     */
    void clear();

    /**
     * Sets the displayed prefix declarations.
     * @param prefixDeclarations A list of {@link PrefixDeclaration}s.
     */
    void setPrefixDeclarations(@Nonnull List<PrefixDeclaration> prefixDeclarations);

    /**
     * Retrieves the prefix names and prefixes currently contained in the view.  This is the
     * list entered by the user and it may contain duplicates and invalid values.
     * @return A map of prefix names to prefixes.
     */
    @Nonnull
    List<PrefixDeclaration> getPrefixDeclarations();
}
