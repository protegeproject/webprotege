package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.stanford.bmir.protege.web.client.ApplicationClientModule;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectIdProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class PrimitiveDataEditorClientModule extends AbstractGinModule {

    public static final int SUGGEST_LIMIT = 20;

    @Override
    protected void configure() {
        install(new ApplicationClientModule());
        bind(PrimitiveDataEditor.class).to(PrimitiveDataEditorImpl.class);
        bind(PrimitiveDataEditorView.class).to(PrimitiveDataEditorViewImpl.class);
        bind(LanguageEditor.class).to(DefaultLanguageEditor.class);
        bind(ProjectId.class).toProvider(ActiveProjectIdProvider.class);
        bind(PrimitiveDataParser.class).to(DefaultPrimitiveDataParser.class);
        bind(EntityDataLookupHandler.class).to(EntityDataLookupHandlerImpl.class);
        bind(FreshEntitiesHandler.class).to(NullFreshEntitiesHandler.class);
        bind(PrimitiveDataEditorFreshEntityView.class).to(PrimitiveDataEditorFreshEntityViewImpl.class);
        bindConstant().annotatedWith(EntitySuggestOracleSuggestLimit.class).to(SUGGEST_LIMIT);
        bindConstant().annotatedWith(EntitySuggestOracleSuggestMode.class).to(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
    }
}
