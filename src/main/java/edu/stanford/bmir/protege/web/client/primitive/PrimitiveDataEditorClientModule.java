package edu.stanford.bmir.protege.web.client.primitive;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxImpl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
@Module
public class PrimitiveDataEditorClientModule {

    public static final int SUGGEST_LIMIT = 20;

    @Provides
    PrimitiveDataEditor providePrimitiveDataEditor(PrimitiveDataEditorImpl editor) {
        return editor;
    }

    @Provides
    PrimitiveDataEditorView providePrimitiveDataEditorView(PrimitiveDataEditorViewImpl view) {
        return view;
    }

    @Provides
    LanguageEditor provideLanguageEditor(DefaultLanguageEditor editor) {
        return editor;
    }

    @Provides
    PrimitiveDataParser providePrimitiveDataParser(PrimitiveDataParserImpl parser) {
        return parser;
    }

    @Provides
    EntityDataLookupHandler provideEntityDataLookupHandler(EntityDataLookupHandlerImpl handler) {
        return handler;
    }

    @Provides
    FreshEntitiesHandler provideFreshEntitiesHandler(NullFreshEntitiesHandler handler) {
        return handler;
    }

    @Provides
    PrimitiveDataEditorFreshEntityView providePrimitiveDataEditorFreshEntityView(PrimitiveDataEditorFreshEntityViewImpl view) {
        return view;
    }

    @Provides
    @EntitySuggestOracleSuggestLimit
    int provideSuggestLimit() {
        return SUGGEST_LIMIT;
    }

    @Provides
    @EntitySuggestOracleSuggestMode
    FreshEntitySuggestMode provideFreshEntitySuggestMode() {
        return FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES;
    }

}