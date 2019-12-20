package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.app.ApplicationInitManager;
import edu.stanford.bmir.protege.web.client.d3.Selection;
import edu.stanford.bmir.protege.web.client.d3.Transform;
import edu.stanford.bmir.protege.web.client.d3.Zoom;
import edu.stanford.bmir.protege.web.client.d3.d3;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.graphlib.Graph2Svg;
import edu.stanford.bmir.protege.web.client.graphlib.NodeDetails;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import elemental.dom.Element;
import elemental.events.Event;
import elemental.events.MouseEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class VizViewImpl extends Composite implements VizView {

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static final double ZOOM_DELTA = 0.05;

    private static final TransformCoordinates DEFAULT_TRANSFORM = TransformCoordinates.get(0, 0, 0.75);

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    private final LinkedHashMap<OWLEntity, TransformCoordinates> previousTransforms = new LinkedHashMap<>();

    private Runnable displaySettingsHandler = () -> {};

    @Nonnull
    private Runnable loadHandler = () -> {
    };

    @UiField
    HTMLPanel viewViewContainer;

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
    @Override
    protected void onLoad() {
        super.onLoad();
        loadHandler.run();
    }


    @Override
    public void setWidget(IsWidget w) {
        viewViewContainer.clear();
        viewViewContainer.add(w);
    }

    @Override
    public void setDisplaySettingsHandler(Runnable displaySettingsHandler) {

    }
}
