package edu.stanford.bmir.protege.web.shared.lang;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class PrefLangChangedEvent extends ProjectEvent<PrefLangChangedHandler> {

    public static final transient Event.Type<PrefLangChangedHandler> PREF_LANG_CHANGED = new Event.Type<>();

    @Nonnull
    private final String prefLang;

    private PrefLangChangedEvent(ProjectId source, @Nonnull String prefLang) {
        super(source);
        this.prefLang = prefLang;
    }

    public static PrefLangChangedEvent get(@Nonnull ProjectId projectId,
                                           @Nonnull String prefLang) {
        return new PrefLangChangedEvent(projectId, prefLang);
    }

    @Nonnull
    public static Event.Type<PrefLangChangedHandler> getType() {
        return PREF_LANG_CHANGED;
    }

    @Override
    public Event.Type<PrefLangChangedHandler> getAssociatedType() {
        return PREF_LANG_CHANGED;
    }

    @Override
    protected void dispatch(PrefLangChangedHandler handler) {
        handler.handlePrefLangChanged(this);
    }

    @Nonnull
    public String getPrefLang() {
        return prefLang;
    }
}
