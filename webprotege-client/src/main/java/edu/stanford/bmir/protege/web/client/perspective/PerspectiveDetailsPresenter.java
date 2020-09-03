package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.*;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectiveDetailsPresenter implements ObjectPresenter<PerspectiveDetails> {

    @Nonnull
    private final PerspectiveDetailsView view;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Nonnull
    private Optional<PerspectiveDetails> current = Optional.empty();

    @Nonnull
    private Consumer<String> headerLabelChangedHandler = label -> {};

    private UuidV4Provider uuidV4Provider;

    @Inject
    public PerspectiveDetailsPresenter(@Nonnull PerspectiveDetailsView view,
                                       @Nonnull LanguageMapCurrentLocaleMapper localeMapper,
                                       @Nonnull UuidV4Provider uuidV4Provider) {
        this.view = checkNotNull(view);
        this.localeMapper = checkNotNull(localeMapper);
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setLabelChangedHandler(() -> headerLabelChangedHandler.accept(getHeaderLabel()));
    }

    @Override
    public void setValue(@Nonnull PerspectiveDetails value) {
        this.current = Optional.of(value);
        view.setLabel(value.getLabel());
        view.setFavorite(value.isFavorite());
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDetails> getValue() {
        PerspectiveId perspectiveId = current.map(PerspectiveDetails::getPerspectiveId)
               .orElseGet(() -> PerspectiveId.get(uuidV4Provider.get()));
        Node layout = current.flatMap(PerspectiveDetails::getLayout).orElse(null);
        return Optional.of(PerspectiveDetails.get(perspectiveId, view.getLabel(), view.isFavorite(), layout));
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return localeMapper.getValueForCurrentLocale(view.getLabel());
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        this.headerLabelChangedHandler = checkNotNull(headerLabelHandler);
    }
}
