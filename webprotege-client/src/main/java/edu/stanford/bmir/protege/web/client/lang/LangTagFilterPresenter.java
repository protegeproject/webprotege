package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.library.tokenfield.AddTokenCallback;
import edu.stanford.bmir.protege.web.client.library.tokenfield.TokenFieldPresenter;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsAction;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashSet;
import java.util.Set;

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
        this.langTagTokenPresenter.setPlaceholder("Language filter");
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
        langTagTokenPresenter.setAddTokenPrompt(this::handleAddLangTag);
        langTagTokenPresenter.setTokensChangedHandler((tokens) -> langTagFilterChangedHandler.handleLangTagFilterChanged());
    }

    public void setPlaceholder(String placeholder) {
        langTagTokenPresenter.setPlaceholder(placeholder);
    }

    private void handleAddLangTag(ClickEvent event, AddTokenCallback<LangTag> callback) {
        Set<LangTag> existingLangTags = ImmutableSet.copyOf(langTagTokenPresenter.getTokenObjects());
        PopupMenu popupMenu = new PopupMenu();
        dispatchServiceManager.execute(new GetProjectLangTagsAction(projectId),
                                       result -> {
                                           result.getLangTags()
                                                 .stream()
                                                 .filter(langTag -> !existingLangTags.contains(langTag))
                                                 .forEach(langTag ->
                                                                  popupMenu.addItem(langTag.format(), () -> {
                                                                        callback.addToken(langTag, langTag.format());
                                                 }));
                                           if(popupMenu.isEmpty()) {
                                               UIAction action = new AbstractUiAction("No remaining languages") {
                                                   @Override
                                                   public void execute() {

                                                   }
                                               };
                                               action.setEnabled(false);
                                               popupMenu.addItem(action);
                                           }
                                           popupMenu.showRelativeTo(view.asWidget());
                                       });
    }

    public interface LangTagFilterChangedHandler {

        void handleLangTagFilterChanged();
    }
}
