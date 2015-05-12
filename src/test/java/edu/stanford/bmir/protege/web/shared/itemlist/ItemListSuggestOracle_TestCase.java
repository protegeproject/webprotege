package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.itemlist.ItemListSuggestOracle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemListSuggestOracle_TestCase<T> {


    private ItemListSuggestOracle<T> oracle;

    @Mock
    private CursorPositionProvider cursorPositionProvider;

    @Mock
    private ItemNameAtCursorParser itemNameAtCursorParser;

    @Mock
    private GetPossibleItemCompletionsActionFactory<T> actionFactory;

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    private ItemRenderer<T> itemRenderer;

    @Mock
    private GetPossibleItemCompletionsAction<T> action;

    @Mock
    private T item;


    @Mock
    private SuggestOracle.Request request;

    @Mock
    private SuggestOracle.Callback callback;



    private static final String ITEMS_STRING = "AItem\nBSuffix\nCItem\n";

    private static final String ITEM_REPLACEMENT_STRING = "BItem";

    private static final String EXPECTED_REPLACEMENT_STRING = "AItem\nBItem\nCItem";

    private static final int CURSOR_POSITION = 8;  // After the B character.


    @Before
    public void setUp() throws Exception {
        oracle = new ItemListSuggestOracle<>(
                cursorPositionProvider,
                itemNameAtCursorParser,
                actionFactory,
                dispatchServiceManager,
                itemRenderer);

        when(cursorPositionProvider.getCursorPosition()).thenReturn(CURSOR_POSITION);
        when(itemNameAtCursorParser.parseItemNameAtCursor(ITEMS_STRING, CURSOR_POSITION)).thenReturn("B");
        when(actionFactory.createGetPossibleItemCompletionsAction(anyString())).thenReturn(action);
        when(itemRenderer.getDisplayString(item)).thenReturn(ITEM_REPLACEMENT_STRING);
        when(itemRenderer.getReplacementString(item)).thenReturn(ITEM_REPLACEMENT_STRING);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                DispatchServiceCallback cb = (DispatchServiceCallback) (invocationOnMock.getArguments()[1]);
                cb.onSuccess(new GetPossibleItemCompletionsResult<T>(Arrays.asList(item)){});
                return null;
            }
        }).when(dispatchServiceManager).execute(any(GetPossibleItemCompletionsAction.class), Mockito.any(DispatchServiceCallback.class));

        when(request.getQuery()).thenReturn(ITEMS_STRING);
    }

    @Test
    public void shouldGenerateCorrectSuggestion() {
        oracle.requestSuggestions(request, callback);

        ArgumentCaptor<SuggestOracle.Response> responseCaptor = ArgumentCaptor.forClass(SuggestOracle.Response.class);
        verify(callback, times(1)).onSuggestionsReady(any(SuggestOracle.Request.class), responseCaptor.capture());

        SuggestOracle.Response actualResponse = responseCaptor.getValue();

        Collection<? extends SuggestOracle.Suggestion> suggestions = actualResponse.getSuggestions();
        assertThat(suggestions.size(), is(1));
        SuggestOracle.Suggestion suggestion = suggestions.iterator().next();
        assertThat(suggestion.getDisplayString(), is(ITEM_REPLACEMENT_STRING));
        assertThat(suggestion.getReplacementString(), is(EXPECTED_REPLACEMENT_STRING));
    }
}
