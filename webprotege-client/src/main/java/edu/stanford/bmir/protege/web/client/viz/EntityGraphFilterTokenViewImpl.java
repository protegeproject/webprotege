package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.viz.FilterName;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class EntityGraphFilterTokenViewImpl extends Composite implements EntityGraphFilterTokenView {

    interface EntityGraphFilterTokenViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphFilterTokenViewImpl> {

    }

    private static EntityGraphFilterTokenViewImplUiBinder ourUiBinder = GWT.create(
            EntityGraphFilterTokenViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public EntityGraphFilterTokenViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTokenFieldContainer() {
        return container;
    }

    @Override
    public void promptChoice(@Nonnull List<FilterName> filterNames,
                             int clientX, int clientY,
                             @Nonnull Consumer<FilterName> chosenFilterConsumer) {
        PopupMenu popupMenu = new PopupMenu();
        filterNames.forEach(filterName -> popupMenu.addItem(filterName.getName(),
                                                            () -> chosenFilterConsumer.accept(filterName)));

        popupMenu.show(clientX, clientY);
    }
}
