package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.library.tokenfield.AddTokenCallback;
import edu.stanford.bmir.protege.web.client.library.tokenfield.TokenFieldPresenter;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsAction;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class LangTagFilterPresenter {

    private final LangTagFilterView view;

    private final ProjectId projectId;

    @Nonnull
    private final TokenFieldPresenter<LangTag> langTagTokenPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private LangTagFilterChangedHandler langTagFilterChangedHandler = () -> {
    };

    @Inject
    public LangTagFilterPresenter(LangTagFilterView view,
                                  ProjectId projectId,
                                  @Nonnull TokenFieldPresenter<LangTag> langTagTokenPresenter,
                                  @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = checkNotNull(view);
        this.projectId = checkNotNull(projectId);
        this.langTagTokenPresenter = checkNotNull(langTagTokenPresenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Nonnull
    public LangTagFilter getFilter() {
        return LangTagFilter.get(ImmutableSet.copyOf(langTagTokenPresenter.getTokenObjects()));
    }

    public void setFilter(@Nonnull LangTagFilter langTagFilter) {
        langTagTokenPresenter.clear();
        langTagFilter.getFilteringTags()
                     .forEach(lt -> langTagTokenPresenter.addToken(lt, lt.format()));
    }

    public void setLangTagFilterChangedHandler(@Nonnull LangTagFilterChangedHandler langTagFilterChangedHandler) {
        this.langTagFilterChangedHandler = checkNotNull(langTagFilterChangedHandler);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        langTagTokenPresenter.start(view.getLangTagsContainer());
        langTagTokenPresenter.setPlaceholder("Language filter");
        langTagTokenPresenter.setAddTokenPrompt(this::handleAddLangTag);
        langTagTokenPresenter.setTokensChangedHandler((tokens) -> langTagFilterChangedHandler.handleLangTagFilterChanged());
    }

    private void handleAddLangTag(ClickEvent event, AddTokenCallback<LangTag> callback) {
        PopupMenu popupMenu = new PopupMenu();
        dispatchServiceManager.execute(new GetProjectLangTagsAction(projectId),
                                       result -> {
                                           result.getLangTags()
                                                 .forEach(langTag ->
                                                                  popupMenu.addItem(langTag.format(), () -> {
                                                                        callback.addToken(langTag, langTag.format());
                                                 }));
                                           popupMenu.showRelativeTo(view.asWidget());
                                       });
    }

    public interface LangTagFilterChangedHandler {

        void handleLangTagFilterChanged();
    }
}
