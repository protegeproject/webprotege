package edu.stanford.bmir.protege.web.shared.lang;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class DisplayLanguageChangedEvent extends ProjectEvent<PrefLangChangedHandler> {

    public static final transient Event.Type<PrefLangChangedHandler> ON_DISPLAY_LANGUAGE_CHANGED = new Event.Type<>();

    @Nonnull
    private final DisplayDictionaryLanguage  displayLang;

    private DisplayLanguageChangedEvent(@Nonnull ProjectId source,
                                        @Nonnull DisplayDictionaryLanguage displayLang) {
        super(source);
        this.displayLang = checkNotNull(displayLang);
    }

    public static DisplayLanguageChangedEvent get(@Nonnull ProjectId projectId,
                                                  @Nonnull DisplayDictionaryLanguage prefLang) {
        return new DisplayLanguageChangedEvent(projectId, prefLang);
    }

    @Nonnull
    public static Event.Type<PrefLangChangedHandler> getType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @Override
    public Event.Type<PrefLangChangedHandler> getAssociatedType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @Override
    protected void dispatch(PrefLangChangedHandler handler) {
        handler.handlePrefLangChanged(this);
    }

    @Nonnull
    public DisplayDictionaryLanguage getDisplayLanguage() {
        return displayLang;
    }
}
