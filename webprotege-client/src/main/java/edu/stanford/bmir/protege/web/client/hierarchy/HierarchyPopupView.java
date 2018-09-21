package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Sep 2018
 */
public interface HierarchyPopupView extends IsWidget {

    void setSelectionChangedHandler(Consumer<EntityNode> handler);

    void setModel(@Nonnull EntityHierarchyModel model);

    void revealEntity(@Nonnull OWLEntity selectedEntity);

    void setDisplayNameSettings(@Nonnull DisplayNameSettings settings);
}
