package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.collect.Sets;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import java.util.ArrayList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/01/15
 */
public class GwtTest_PrimitiveDataSuggestOracle extends GWTTestCase {

    private PrimitiveDataEditorSuggestOracle oracle;

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        delayTestFinish(10000);

        oracle = new PrimitiveDataEditorSuggestOracle(new EntitySuggestOracleStub(), FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES);
        oracle.setFreshEntityStrategy(new SimpleFreshEntitySuggestStrategy());
    }

    /**
     * If a literal is one of the possible types then no create suggestions should be shown.  The literal
     * should just be accepted.
     */
    public void test_shouldNotOfferCreateSuggestionsIfLiteralsAreAllowed() {
        oracle.setAllowedPrimitiveTypes(Sets.newHashSet(PrimitiveType.LITERAL, PrimitiveType.DATA_TYPE));
        final int expectedSuggestionSize = 0;
        performRequestSuggestions(expectedSuggestionSize);
    }

    /**
     * If a literal is not one of the possible types then create suggestions should be shown for the other types.
     */
    public void test_shouldOfferCreateSuggestionsIfLiteralsAreNotAllowed() {
        oracle.setAllowedPrimitiveTypes(Sets.newHashSet(PrimitiveType.CLASS, PrimitiveType.ANNOTATION_PROPERTY, PrimitiveType.DATA_TYPE));
        final int expectedSuggestionSize = 3;
        performRequestSuggestions(expectedSuggestionSize);
    }

    /**
     * Request suggestions and test outcome is the expected size.
     * @param expectedSuggestionSize The expected size.
     */
    private void performRequestSuggestions(final int expectedSuggestionSize) {
        oracle.requestSuggestions(new SuggestOracle.Request("Hello", 10), new SuggestOracle.Callback() {
            @Override
            public void onSuggestionsReady(SuggestOracle.Request request, SuggestOracle.Response response) {
                int suggestionsSize = response.getSuggestions().size();
                if (suggestionsSize == expectedSuggestionSize) {
                    finishTest();
                } else {
                    fail("Expected " + expectedSuggestionSize + " suggestions but found " + suggestionsSize);
                }
            }
        });
    }


    /**
     * An EntitySuggestOracle stub that always returns empty suggestions.
     */
    private static class EntitySuggestOracleStub extends EntitySuggestOracle {

        public EntitySuggestOracleStub() {
            super(null, 10, null);
        }

        @Override
        public void requestSuggestions(Request request, Callback callback) {
            callback.onSuggestionsReady(request, new Response(new ArrayList<Suggestion>()));
        }
    }
}
