package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.viz.FilterName;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public interface EntityGraphFilterTokenView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getTokenFieldContainer();

    void promptChoice(@Nonnull List<FilterName> filterNames,
                      int clientX, int clientY,
                      @Nonnull Consumer<FilterName> chosenFilterConsumer);
}
